package dk.jonaslindstrom.ruffini.common.helpers;

import java.util.function.BinaryOperator;

/**
 * Instances of this class allows for easy repeated application of a binary operator on many
 * operands.
 */
public class MultiOperator<E> {

    private final BinaryOperator<E> operator;

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
