package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

/**
 * This function computes the product of two matrices.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class MatrixMultiplication<E> implements BinaryOperator<Matrix<E>> {

  private BiFunction<Vector<E>, Vector<E>, E> dotProduct;

  public MatrixMultiplication(Ring<E> ring) {
    this.dotProduct = new DotProduct<>(ring);
  }

  public MatrixMultiplication(BiFunction<Vector<E>, Vector<E>, E> dotProduct) {
    this.dotProduct = dotProduct;
  }

  public MatrixMultiplication(BinaryOperator<E> add, BinaryOperator<E> multiplication) {
    this.dotProduct = new DotProduct<>(add, multiplication);
  }

  @Override
  public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
    assert (a.getWidth() == b.getHeight());

    return Matrix.of(a.getHeight(), b.getWidth(), (i, j) -> {
      return dotProduct.apply(a.getRow(i), b.getColumn(j));
    });
  }

}
