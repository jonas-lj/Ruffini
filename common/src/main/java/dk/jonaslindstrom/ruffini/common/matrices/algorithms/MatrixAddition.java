package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;

import java.util.function.BinaryOperator;

/**
 * Compute the sum of two matrices.
 */
public class MatrixAddition<E> implements BinaryOperator<Matrix<E>> {

    private final BinaryOperator<E> add;

    public MatrixAddition(AdditiveGroup<E> group) {
        this(group::add);
    }

    public MatrixAddition(BinaryOperator<E> add) {
        this.add = add;
    }

    @Override
    public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
        if (a.getHeight() != b.getHeight() || a.getWidth() != b.getWidth()) {
            throw new IllegalArgumentException("Matrices must have same size but was " + a.getHeight() +
                    "×" + a.getWidth() + " and " + b.getHeight() + "×" + b.getWidth());
        }

        return Matrix.of(a.getHeight(), a.getWidth(), (i, j) -> add.apply(a.get(i, j), b.get(i, j)));
    }

}
