import com.google.common.collect.ImmutableSortedMap;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.*;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingFFT;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingKaratsuba;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

public class PolynomialTests {

    @Test
    public void testBatchEvaluation() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(51));
        PolynomialRing<Integer> polynomialRing = new PolynomialRingKaratsuba<>(field);

        int n = 16;

        Polynomial<Integer> p = Polynomial.of(5, 12, 1, 7, 5, 5, 12, 1, 7, 5);
        List<Integer> inputs = IntStream.range(0, n).boxed().toList();
        BatchPolynomialEvaluation<Integer> batchEvaluation = new BatchPolynomialEvaluation<>(polynomialRing, inputs);

        field.reset();
        List<Integer> results = batchEvaluation.apply(p);
        System.out.println(field);

        field.reset();
        List<Integer> expected = inputs.stream().mapToInt(x -> p.apply(x, field)).boxed().toList();
        System.out.println();
        System.out.println(field);
        Assert.assertEquals(expected, results);
    }

    @Test
    public void testInterpolation() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(23));

        List<Integer> x = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        List<Integer> y = List.of(1, 2, 1, 4, 1, 2, 3, 4, 1, 2, 1, 4, 1, 2, 3, 4);

        PolynomialRing<Integer> polynomialRing = new PolynomialRingKaratsuba<>(field);
        PolynomialInterpolation<Integer> interpolate = new PolynomialInterpolation<>(polynomialRing, x);
        field.reset();
        Polynomial<Integer> p = interpolate.apply(y);
        System.out.println(field);
        for (int i = 0; i < x.size(); i++) {
            Assert.assertEquals(y.get(i), p.apply(x.get(i), field));
        }
        field.reset();

        Polynomial<Integer> lagrange = new LagrangePolynomial<>(field).apply(x, y);
        System.out.println();
        System.out.println(field);

        Assert.assertEquals(p, lagrange);
    }

    @Test
    public void testFFT() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(13));

        Polynomial<Integer> p = Polynomial.of(2, 4, 5);
        Polynomial<Integer> q = Polynomial.of(1, 2, 3, 4);

        PolynomialRing<Integer> polynomialRingFft = new PolynomialRingFFT<>(field, ImmutableSortedMap.of(3, 3, 2, 12, 6, 4, 4, 5));
        Polynomial<Integer> actual = polynomialRingFft.multiply(p, q);
        System.out.println("With FFT");
        System.out.println(field);
        field.reset();

        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);
        Polynomial<Integer> expected = polynomialRing.multiply(p, q);
        System.out.println();
        System.out.println("Without FFT");
        System.out.println(field);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testKaratsubaAlgorithm() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(13));
        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);

        Polynomial<Integer> p = Polynomial.of(2, 3, 4, 5, 6 );
        Polynomial<Integer> q = Polynomial.of(1, 2, 3, 4, 7, 9, 1 );

        KaratsubaAlgorithm<Integer> karatsuba = new KaratsubaAlgorithm<>(polynomialRing);
        field.reset();
        Polynomial<Integer> actual = karatsuba.apply(p, q);
        System.out.println(field);
        System.out.println();
        field.reset();
        Polynomial<Integer> expected = polynomialRing.multiply(p, q);
        System.out.println(field);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInversion() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(7));
        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);

        Polynomial<Integer> f = Polynomial.of(1, 2, 3);

        Polynomial<Integer> g = new Inversion<>(field).apply(f, 4);

        System.out.println(g);
        System.out.println(polynomialRing.multiply(f,g));
    }

    @Test
    public void testFastDivision() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(7));
        PolynomialRing<Integer> polynomialRing = new PolynomialRingKaratsuba<>(field);

        Polynomial<Integer> f = Polynomial.of(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6);
        Polynomial<Integer> g = Polynomial.of(1, 2, 3, 4, 5, 1, 2, 3, 4, 5);

        field.reset();
        Pair<Polynomial<Integer>, Polynomial<Integer>> result = new FastDivision<>(polynomialRing).apply(f, g);
        System.out.println(field);

        System.out.println();

        field.reset();
        result = polynomialRing.divide(f, g);
        System.out.println(field);

        System.out.println(polynomialRing.add(result.second, polynomialRing.multiply(result.first, g)));

    }

}
