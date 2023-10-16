package demo;

import dk.jonaslindstrom.ruffini.common.algorithms.Product;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.BatchPolynomialEvaluation;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingKaratsuba;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

public class CauchyMatrix {

    public static void main(String[] arguments) {
        Random random = new Random(1234);
        int n = 16;
        BigInteger modulus = new BigInteger("2523648240000001BA344D80000000086121000000000013A700000000000013", 16);
        PerformanceLoggingField<BigInteger> bn254 = new PerformanceLoggingField<>(new BigPrimeField(modulus));

        // Sample random Cauchy matrix
        List<BigInteger> s = ArrayUtils.populate(n, i -> SamplingUtils.sample(modulus, random));
        List<BigInteger> t = ArrayUtils.populate(n, i -> SamplingUtils.sample(modulus, random));

        // Use Karatsubas algorithm
        PolynomialRing<BigInteger> polynomialRing = new PolynomialRingKaratsuba<>(bn254);

        // f = (x-t_0)...(x-t_{n-1})
        Polynomial<BigInteger> f = new Product<>(polynomialRing).apply(ArrayUtils.populate(n, i -> Polynomial.of(bn254.negate(t.get(i)), bn254.identity())));
        Polynomial<BigInteger> fPrime = f.differentiate(bn254);

        // Sample random vector b
        Vector<BigInteger> b = Vector.of(n, i -> SamplingUtils.sample(modulus, random));

        // Compute the Cauchy matrix C
        Matrix<BigInteger> C = Matrix.of(n, n, (i, j) -> bn254.invert(bn254.subtract(s.get(i), t.get(j))));
        bn254.reset();

        // Compute the matrix to vector product Cb and print the number of field operations used.
        Vector<BigInteger> expected = C.apply(b, bn254);
        System.out.println("Baseline:");
        System.out.println(bn254);

        // Precomputations
        Vector<BigInteger> fInverses = Vector.of(n, i -> bn254.invert(f.apply(s.get(i), bn254)));
        Vector<BigInteger> fPrimeEvaluated = Vector.of(n, i -> fPrime.apply(t.get(i), bn254));
        PolynomialInterpolation<BigInteger> interpolate = new PolynomialInterpolation<>(polynomialRing, t);
        BatchPolynomialEvaluation<BigInteger> evaluate = new BatchPolynomialEvaluation<>(polynomialRing, s);

        bn254.reset();

        // Compute the product Cb using the formula of expressing a Cauchy matrix via Vandermonde matrices.
        Vector<BigInteger> x1 = fPrimeEvaluated.coordinateWise(b, bn254::multiply);
        Vector<BigInteger> x2 = interpolate.apply(x1.asList()).vector(BigInteger.ZERO);
        Vector<BigInteger> x3 = Vector.ofList(evaluate.apply(new Polynomial<>(x2, bn254)));
        Vector<BigInteger> x4 = Vector.of(n, i -> bn254.multiply(x3.get(i), fInverses.get(i)));

        // Print the number of field operations used.
        System.out.println();
        System.out.println("Using Vandermonde matrices:");
        System.out.println(bn254);

        System.out.println();
        System.out.println("Matches");
        System.out.println(expected.asList().equals(x4.asList()));
    }


}
