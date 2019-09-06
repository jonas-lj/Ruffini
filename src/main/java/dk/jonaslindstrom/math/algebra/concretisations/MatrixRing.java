package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.MatrixAddition;
import dk.jonaslindstrom.math.algebra.algorithms.StrassenMultiplication;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.function.BinaryOperator;

/**
 * This class represents a ring of matrices over a base ring.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class MatrixRing<E> implements Ring<Matrix<E>> {

  private Ring<E> baseRing;
  private int dimension;
  private BinaryOperator<Matrix<E>> multiplication, addition;

  public MatrixRing(Ring<E> baseRing, int dimension) {
    this.baseRing = baseRing;
    this.dimension = dimension;
    this.multiplication = new StrassenMultiplication<>(baseRing);
    this.addition = new MatrixAddition<>(baseRing);
  }

  @Override
  public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
    return multiplication.apply(a,b);
  }

  @Override
  public Matrix<E> getIdentity() {
    return Matrix.of(dimension, dimension,
        (i, j) -> i == j ? baseRing.getIdentity() : baseRing.getZero());
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
    return a.forEach(e -> baseRing.negate(e));
  }

  @Override
  public Matrix<E> getZero() {
    return Matrix.of(dimension, dimension, baseRing.getZero());
  }
  
  @Override
  public String toString() {
    return "M" + StringUtils.subscript(String.valueOf(dimension)) + "(" + baseRing + ")";
  }

}
