import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.PolynomialInterpolation;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

        List<Integer> x = List.of(1, 2, 3, 4);
        List<Integer> y = List.of(1, 2, 1, 4);

        Polynomial<Integer> p = new PolynomialInterpolation<>(field).apply(x, y);

        for (int i = 0; i < x.size(); i++) {
            Assert.assertEquals(y.get(i), p.apply(x.get(i), field));
        }

    }

}
