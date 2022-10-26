package dk.jonaslindstrom.ruffini.finitefields.algorithms;

import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.finitefields.FiniteField;
import dk.jonaslindstrom.ruffini.finitefields.PrimeField;
import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class TonelliShanks implements UnaryOperator<Polynomial<Integer>> {

    private final FiniteField field;
    private final int p, m, q;
    private final Power<Polynomial<Integer>> power;
    private final Random random;

    public TonelliShanks(FiniteField field) {
        // https://eprint.iacr.org/2012/685.pdf
        this.field = field;
        this.p = field.getPrime();
        this.m = field.getExponent();
        this.power = new Power<>(field);
        this.q = new Power<>(Integers.getInstance()).apply(p, m);
        this.random = new Random();
    }

    public static UnaryOperator<Integer> forPrimeField(PrimeField field) {
        TonelliShanks sqrt = new TonelliShanks(field.asFiniteField());
        return i -> sqrt.apply(Polynomial.constant(i)).getCoefficient(0);
    }

    private Polynomial<Integer> sampleUnit() {
        Polynomial.Builder<Integer> builder = new Polynomial.Builder<>(Integers.getInstance());
        IntStream.range(0, m).forEach(i -> builder.set(i, random.nextInt(p)));
        Polynomial<Integer> polynomial = builder.build();

        if (polynomial.equals(field.getZero())) {
            return sampleUnit();
        }

        return polynomial;
    }

    @Override
    public Polynomial<Integer> apply(Polynomial<Integer> a) {

        if (field.equals(a, field.getZero())) {
            return field.getZero();
        }

        if (field.equals(a, field.getIdentity())) {
            return field.getIdentity();
        }

        if (this.p == 2) {
            return power.apply(a, 1 << (q / 2));
        }

        int t = q - 1;
        int s = 0;

        while (Math.floorMod(t, 2) == 0) {
            t = t / 2;
            s = s + 1;
        }

        Polynomial<Integer> c0;
        Polynomial<Integer> z;
        do {
            Polynomial<Integer> c = sampleUnit();
            z = power.apply(c, t);
            c0 = power.apply(c, 1 << (s - 1));
        } while (c0.equals(field.getIdentity()));

        Polynomial<Integer> ω = power.apply(a, (t - 1) / 2);
        Polynomial<Integer> a0 = power.apply(field.multiply(ω, ω, a), 1 << (s - 1));

        if (field.equals(a0, field.negate(field.getIdentity()))) {
            throw new IllegalArgumentException("Input is not a square.");
        }

        int v = s;
        Polynomial<Integer> x = field.multiply(a, ω);
        Polynomial<Integer> b = field.multiply(x, ω);

        Polynomial<Integer> bPower = b;
        while (!field.equals(b, field.getIdentity())) {
            int k = 0;
            do {
                k = k + 1;
                bPower = field.multiply(bPower, bPower);
            } while (!field.equals(bPower, field.getIdentity()));

            if (k == v) {
                // Try again
                return apply(a);
            }

            ω = power.apply(z, 1 << (v - k - 1));
            z = field.multiply(ω, ω);
            b = field.multiply(b, z);
            x = field.multiply(x, ω);
            v = k;
        }

        return x;
    }
}
