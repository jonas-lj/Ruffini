package dk.jonaslindstrom.ruffini.common.matrices.algorithms;


import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;

import java.util.function.UnaryOperator;

/**
 * Compute the inverse of a matrix.
 */
public class MatrixInversion<E> implements UnaryOperator<Matrix<E>> {

    private final Field<E> field;

    public MatrixInversion(Field<E> field) {
        this.field = field;
    }

    @Override
    public Matrix<E> apply(Matrix<E> a) {
        assert a.isSquare();
        int n = a.getHeight();
        Matrix<E> composed = Matrix.lazy(n, 2 * n, (i, j) -> {
            if (j < n) {
                return a.get(i, j);
            } else {
                int k = j - n;
                return i == k ? field.identity() : field.zero();
            }
        });

        Matrix<E> reduced = new GaussianElimination<>(field).apply(composed);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j && !reduced.get(i, j).equals(field.identity()) || i != j && !reduced.get(i, j).equals(field.zero())) {
                    throw new IllegalArgumentException("Matrix not invertible");
                }
            }
        }

        return reduced.submatrix(0, n, n, 2 * n);
    }
}
