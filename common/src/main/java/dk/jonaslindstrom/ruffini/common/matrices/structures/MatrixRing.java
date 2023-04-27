package dk.jonaslindstrom.ruffini.common.matrices.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.MatrixAddition;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.StrassenMultiplication;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.StringUtils;

import java.util.function.BinaryOperator;

/**
 * This class represents a ring of matrices over a base ring.
 */
public class MatrixRing<E> implements Ring<Matrix<E>> {

    private final Ring<E> baseRing;
    private final int dimension;
    private final BinaryOperator<Matrix<E>> multiplication;
    private final BinaryOperator<Matrix<E>> addition;

    public MatrixRing(Ring<E> baseRing, int dimension) {
        this.baseRing = baseRing;
        this.dimension = dimension;
        this.multiplication = new StrassenMultiplication<>(baseRing);
        this.addition = new MatrixAddition<>(baseRing);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        return multiplication.apply(a, b);
    }

    @Override
    public Matrix<E> identity() {
        return Matrix.of(dimension, dimension,
                (i, j) -> i.equals(j) ? baseRing.identity() : baseRing.zero());
    }

    @Override
    public String toString(Matrix<E> a) {
        return null;
    }

    @Override
    public boolean equals(Matrix<E> a, Matrix<E> b) {
        return a.equals(b, baseRing::equals);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return addition.apply(a, b);
    }

    @Override
    public Matrix<E> negate(Matrix<E> a) {
        return a.map(baseRing::negate);
    }

    @Override
    public Matrix<E> zero() {
        return Matrix.of(dimension, dimension, baseRing.zero());
    }

    @Override
    public String toString() {
        return "M" + StringUtils.subscript(String.valueOf(dimension)) + "(" + baseRing + ")";
    }
}
