package dk.jonaslindstrom.arithmeticparser;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import dk.jonaslindstrom.arithmeticparser.Evaluator;
import dk.jonaslindstrom.arithmeticparser.MultiOperator;

public class TestParser {

  private static Double func(int x, int y) {
    return func((double) x, (double) y);
  }

  private static Double func(Double x, Double y) {
    return 3 * x + y;
  }

  @Test
  public void testParser() {

    String e = "(-2+f(3,(3+(-2))y)) / 2";

    Map<String, Double> variables = Collections.singletonMap("y", 3.0);
    Map<String, MultiOperator<Double>> functions =
        Collections.singletonMap("f", new MultiOperator<Double>(TestParser::func));

    double value = Evaluator.evaluate(e, variables, functions);

    int y = 3;
    double expected = (-2 + func(3, (3 + (-2)) * y)) / 2;

    Assert.assertTrue(value == expected);
  }

}
