import com.google.common.collect.ImmutableSortedMap;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.BatchPolynomialEvaluation;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.LagrangePolynomial;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingFFT;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

public class PolynomialTests {

    @Test
    public void testBatchEvaluation() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(51));
        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);

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
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(13));

        List<Integer> x = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> y = List.of(1, 2, 1, 4, 1, 2, 3, 4);

        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);
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

}
