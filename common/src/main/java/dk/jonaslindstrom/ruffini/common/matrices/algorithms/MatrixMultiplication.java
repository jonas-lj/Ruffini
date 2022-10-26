package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.DotProduct;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * This function computes the product of two matrices.
 */
public class MatrixMultiplication<E> implements BinaryOperator<Matrix<E>> {

    private final BiFunction<Vector<E>, Vector<E>, E> dotProduct;

    public MatrixMultiplication(Ring<E> ring) {
        this(new DotProduct<>(ring));
    }

    public MatrixMultiplication(BiFunction<Vector<E>, Vector<E>, E> dotProduct) {
        this.dotProduct = dotProduct;
    }

    @Override
    public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
        if (a.getWidth() != b.getHeight()) {
            throw new IllegalArgumentException("Matrix sizes does allow multiplication.");
        }

        return Matrix
                .of(a.getHeight(), b.getWidth(), (i, j) -> dotProduct.apply(a.getRow(i), b.getColumn(j)));
    }

}
