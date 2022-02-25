package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import java.util.function.BinaryOperator;

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
