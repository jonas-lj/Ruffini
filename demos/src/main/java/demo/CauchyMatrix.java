package demo;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.algorithms.Product;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class CauchyMatrix {

    public static void main(String[] arguments) {
        Random random = new Random(1234);
        int n = 8;
        BigInteger modulus = new BigInteger("2523648240000001BA344D80000000086121000000000013A700000000000013", 16);
        Field<BigInteger> bn254 = new BigPrimeField(modulus);

        List<BigInteger> s = ArrayUtils.populate(n, i -> SamplingUtils.sample(modulus, random));
        List<BigInteger> t = ArrayUtils.populate(n, i -> SamplingUtils.sample(modulus, random));
        System.out.println("s = " + s);
        System.out.println("t = " + t);

        PolynomialRing<BigInteger> polynomialRing = new PolynomialRing<>(bn254);
        Polynomial<BigInteger> f = new Product<>(polynomialRing).apply(ArrayUtils.populate(n, i -> Polynomial.of(bn254.negate(t.get(i)), bn254.identity())));
        System.out.println("f = " + f);

        Polynomial<BigInteger> fPrime = f.differentiate(bn254);
        System.out.println("f' = " + fPrime);

        BigInteger phi = BigInteger.valueOf(7);
        System.out.println("phi = " + phi);

        Power<BigInteger> power = new Power<>(bn254);
        for (BigInteger ti : t) {
            if (power.apply(ti, BigInteger.valueOf(n)).equals(phi)) {
                System.out.println("Found phi: " + ti);
                return;
            }
        }

        Polynomial<BigInteger> g = new Polynomial.Builder<>(bn254).set(0, phi).set(n, bn254.negate(bn254.identity())).build();
        System.out.println("g = " + g);

        Vector<BigInteger> b = Vector.of(n, i -> SamplingUtils.sample(modulus, random));
        System.out.println("b = " + b);

        // Expected
        Matrix<BigInteger> C = Matrix.of(n, n, (i, j) -> bn254.invert(bn254.subtract(s.get(i), t.get(j))));
        Vector<BigInteger> expected = C.apply(b, bn254);
        System.out.println("Expected: " + expected);

        // Multiply by f'(t_i)
        Vector<BigInteger> x1 = Vector.of(n, i -> fPrime.apply(t.get(i), bn254)).coordinateWise(b, bn254::multiply);

        Vector<BigInteger> x2 = new PolynomialInterpolation<>(polynomialRing).apply(t, x1.asList()).vector(BigInteger.ZERO);

        Vector<BigInteger> x3 = Vector.ofList(new Polynomial<>(x2, bn254).batchApply(s, polynomialRing));

        Vector<BigInteger> x4 = Vector.of(n, i -> bn254.divide(x3.get(i), f.apply(s.get(i), bn254)));

        System.out.println(x4);
    }


}
