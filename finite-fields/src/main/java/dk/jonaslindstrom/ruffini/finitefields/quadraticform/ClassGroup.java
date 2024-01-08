package dk.jonaslindstrom.ruffini.finitefields.quadraticform;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.common.algorithms.BigLegendreSymbol;
import dk.jonaslindstrom.ruffini.common.algorithms.ChineseRemainderTheorem;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.integers.algorithms.ModularSquareRoot;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;

import java.math.BigInteger;
import java.util.Random;

/**
 * This implements the ideal class group for a negative discriminant.
 */
public class ClassGroup implements Group<QuadraticForm<BigInteger, BigIntegers>> {

    private final BigInteger discriminant;
    private final QuadraticForm<BigInteger, BigIntegers> principal;

    public ClassGroup(BigInteger discriminant) {
        if (discriminant.compareTo(BigInteger.ZERO) >= 0) {
            throw new IllegalArgumentException("Discriminant must be negative");
        }

        this.discriminant = discriminant;

        // Compute identity
        BigInteger k = discriminant.mod(BigInteger.TWO);
        this.principal =
                new QuadraticForm<>(BigIntegers.getInstance(),
                        BigInteger.ONE,
                        k,
                        k.multiply(k).subtract(discriminant).divide(BigInteger.valueOf(4)));
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> invert(QuadraticForm<BigInteger, BigIntegers> a) {
        if (!a.discriminant().equals(discriminant)) {
            throw new IllegalArgumentException("Input has wrong discriminant");
        }
        return new QuadraticForm<>(BigIntegers.getInstance(),
                a.getA(),
                a.getB().negate(),
                a.getC());
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> identity() {
        return principal;
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> multiply(QuadraticForm<BigInteger, BigIntegers> a, QuadraticForm<BigInteger, BigIntegers> b) {
        if (!a.discriminant().equals(discriminant) || !b.discriminant().equals(discriminant)) {
            throw new IllegalArgumentException("Some of the inputs have the wrong discriminant");
        }
        return a.compose(b);
    }

    @Override
    public String toString(QuadraticForm<BigInteger, BigIntegers> a) {
        return a.toString();
    }

    @Override
    public boolean equals(QuadraticForm<BigInteger, BigIntegers> a, QuadraticForm<BigInteger, BigIntegers> b) {
        return a.getA().equals(b.getA()) && a.getB().equals(b.getB()) && a.getC().equals(b.getC());
    }

    public QuadraticForm<BigInteger, BigIntegers> sample(Random random) {
        if (discriminant.mod(BigInteger.valueOf(4)).intValue() != 1 || !discriminant.abs().isProbablePrime(100)) {
            throw new IllegalStateException("Discriminant must be prime and 1 mod 4");
        }

        // Note: The number of candidates can be computed with the prime number theorem.

        // If a is smaller than this bound and |b| < a, the form is guaranteed to be reduced.
        BigInteger bound = discriminant.abs().sqrt().shiftRight(1);

        // Sample a prime a such that a = 3 mod 4 and the discriminant is a quadratic residue mod a
        // TODO: Sample a with factorization and use this to compute the legendre symbol of the discriminant (https://pages.cs.wisc.edu/~cs812-1/pfrn.pdf or https://inst.eecs.berkeley.edu/~cs276/fa20/notes/Kalai_generating_factored.pdf)
        BigInteger a;
        int iterations = 0;
        do {
            a = SamplingUtils.sample(bound.shiftRight(2), random).shiftLeft(2).add(BigInteger.valueOf(3));
            // TODO: Once a square root algorithm is implemented, we may allow a to be 1 mod 4 also
            iterations++;
            // Note: We could also check legendre(a, discriminant.negate()) != 1 due to the law of quadratic reciprocity
        } while (legendre(discriminant, a) != 1 || !a.isProbablePrime(100));
        System.out.println("Iterations: " + iterations);

        // Compute square root of the discriminant mod a
        BigInteger bPrime = new ModularSquareRoot(a).apply(discriminant);
        BigInteger b = bPrime.multiply(a.add(BigInteger.ONE)).subtract(a).mod(a.shiftLeft(2));

        // The "normalization operator" replaces b with b-2a until b <= a.
        // Here, b is < 4a, so we only need to do this at most twice.
        while (b.compareTo(a) > 0) {
            b = b.subtract(a.shiftLeft(1));
        }

        BigInteger c = b.multiply(b).subtract(discriminant).divide(a.shiftLeft(2));
        QuadraticForm<BigInteger, BigIntegers> result = new QuadraticForm<>(BigIntegers.getInstance(), a, b, c);
        System.out.println("Reduced: " + result.isReduced());
        System.out.println("Discriminant: " + result.discriminant().equals(discriminant));
        return result.reduce();
    }

    private static int legendre(BigInteger a, BigInteger p) {
        BigInteger l = a.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.TWO), p);
        return l.equals(BigInteger.ONE) ? 1 : -1;
    }
}
