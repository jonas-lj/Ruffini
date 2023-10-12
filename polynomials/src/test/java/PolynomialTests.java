import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Streams;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.LagrangePolynomial;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingFFT;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PolynomialTests {

    @Test
    public void testBatchEvaluation() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(23));
        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);

        Polynomial<Integer> p = Polynomial.of(5, 12, 1, 7, 5);
        List<Integer> inputs = Arrays.asList(0, 1, 2, 3);
        List<Integer> results = p.batchApply(inputs, polynomialRing);
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
        Polynomial<Integer> p = new PolynomialInterpolation<>(polynomialRing).apply(x, y);
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
    public void testFastMultiplication() {
        PerformanceLoggingField<Integer> field = new PerformanceLoggingField<>(new TestUtils.TestField(13));

        Polynomial<Integer> p = Polynomial.of(2, 4, 5, 2, 4, 5, 2, 4, 5);
        Polynomial<Integer> q = Polynomial.of(1, 2, 3, 1, 2, 3, 1, 2, 3);

        PolynomialRing<Integer> polynomialRing = new PolynomialRing<>(field);
        Polynomial<Integer> expected = polynomialRing.multiply(p, q);
        System.out.println("Baseline");
        System.out.println(field);
        field.reset();

        List<Integer> x = List.of(1, 2, 3, 4, 1, 2, 3, 4);

        List<Integer> px = p.batchApply(x, polynomialRing);
        List<Integer> qx = q.batchApply(x, polynomialRing);
        List<Integer> products = Streams.zip(px.stream(), qx.stream(), field::multiply).toList();
        Polynomial<Integer> actual = new PolynomialInterpolation<>(new PolynomialRing<>(field)).apply(x, products);
        System.out.println();
        System.out.println("Actual");
        System.out.println(field);

        Assert.assertEquals(expected, actual);
    }

}
