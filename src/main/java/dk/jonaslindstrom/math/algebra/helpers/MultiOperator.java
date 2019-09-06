package dk.jonaslindstrom.math.algebra.helpers;

import java.util.function.BinaryOperator;

/**
 * Instances of this class allows for easy repeated application of a binary operator on many
 * operands.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class MultiOperator<E> {

  private BinaryOperator<E> operator;

  public MultiOperator(BinaryOperator<E> operator) {
    this.operator = operator;
  }

  @SafeVarargs
  public final E apply(E... operands) {
    assert (operands.length > 0);

    if (operands.length == 1) {
      return operands[0];
    }

    E c = operator.apply(operands[0], operands[1]);

    for (int i = 2; i < operands.length; i++) {
      c = operator.apply(c, operands[i]);
    }

    return c;
  }

}
