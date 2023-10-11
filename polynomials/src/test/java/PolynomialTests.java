import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.FastPolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PolynomialTests {

    @Test
    public void testBatchEvaluation() {
        TestUtils.TestField field = new TestUtils.TestField(13);

        Polynomial<Integer> p = Polynomial.of(-5, 4, 0, 2, -1, 1);

        List<Integer> inputs = Arrays.asList(0, 1, 2, 3);
        List<Integer> results = p.batchApply(inputs, field);

        List<Integer> expected = inputs.stream().mapToInt(x -> p.apply(x, field)).boxed().toList();
        Assert.assertEquals(expected, results);
    }

    @Test
    public void testInterpolation() {
        TestUtils.TestField field = new TestUtils.TestField(13);

        List<Pair<Integer, Integer>> points = List.of(
                Pair.of(1,1),
                Pair.of(2,2),
                Pair.of(3,1),
                Pair.of(4,5));

        Polynomial<Integer> p = new FastPolynomialInterpolation<>(field).apply(points);

        for (Pair<Integer, Integer> point : points) {
            Assert.assertEquals(point.second, p.apply(point.first, field));
        }

    }

}
