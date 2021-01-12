package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import java.util.function.BinaryOperator;

public class MatrixAddition<E> implements BinaryOperator<Matrix<E>> {

  private final BinaryOperator<E> add;

  public MatrixAddition(Ring<E> ring) {
    this(ring::add);
  }

  public MatrixAddition(BinaryOperator<E> add) {
    this.add = add;
  }

  @Override
  public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
    if (a.getHeight() != b.getHeight() || a.getWidth() != b.getWidth()) {
      throw new IllegalArgumentException("Matrix sizes does not allow addition");
    }

    return Matrix.of(a.getHeight(), a.getWidth(), (i, j) -> add.apply(a.get(i, j), b.get(i, j)));
  }

}
