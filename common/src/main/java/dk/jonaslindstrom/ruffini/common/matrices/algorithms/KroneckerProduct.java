package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;

import java.util.function.BinaryOperator;

/**
 * Compute the Kronecker product of two matrices.
 */
public class KroneckerProduct<E> implements BinaryOperator<Matrix<E>> {

    private final Ring<E> ring;

    public KroneckerProduct(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
        return Matrix.fromBlocks(a.map(aij -> b.map(bij -> ring.multiply(bij, aij))));
    }
}
