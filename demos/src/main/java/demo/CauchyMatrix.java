package demo;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.algorithms.Product;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.BatchPolynomialEvaluationFixed;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.BinaryTree;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolationFixed;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CauchyMatrix {

    public static void main(String[] arguments) {
        Random random = new Random(1234);
        int n = 16;
        BigInteger modulus = new BigInteger("2523648240000001BA344D80000000086121000000000013A700000000000013", 16);
        PerformanceLoggingField<BigInteger> bn254 = new PerformanceLoggingField<>(new BigPrimeField(modulus));

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
        bn254.reset();
        Vector<BigInteger> expected = C.apply(b, bn254);
        System.out.println(bn254);

        System.out.println("Expected: " + expected);

        Vector<BigInteger> fInverses = Vector.of(n, i -> bn254.invert(f.apply(s.get(i), bn254)));

        Vector<BigInteger> fPrimeEvaluated = Vector.of(n, i -> fPrime.apply(t.get(i), bn254));
        // Multiply by f'(t_i)

        PolynomialInterpolationFixed<BigInteger> interpolate = new PolynomialInterpolationFixed<>(polynomialRing, t);
        BatchPolynomialEvaluationFixed<BigInteger> evaluate = new BatchPolynomialEvaluationFixed<>(polynomialRing, s);

        bn254.reset();

        Vector<BigInteger> x1 = fPrimeEvaluated.coordinateWise(b, bn254::multiply);
        System.out.println(bn254);

        Vector<BigInteger> x2 = interpolate.apply(x1.asList()).vector(BigInteger.ZERO);
        System.out.println(bn254);

        Vector<BigInteger> x3 = Vector.ofList(evaluate.apply(new Polynomial<>(x2, bn254)));
        System.out.println(bn254);

        Vector<BigInteger> x4 = Vector.of(n, i -> bn254.multiply(x3.get(i), fInverses.get(i)));
        System.out.println(bn254);

        System.out.println(expected.asList().equals(x4.asList()));
    }


}
