package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.Group;
import dk.jonaslindstrom.math.algebra.algorithms.MatrixInversion;
import dk.jonaslindstrom.math.algebra.algorithms.MatrixMultiplication;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class GeneralLinearGroup<E, F extends Field<E>> implements Group<Matrix<E>> {

  private final F field;
  private final int n;
  private final UnaryOperator<Matrix<E>> inversion;
  private final BinaryOperator<Matrix<E>> multiplication;

  public GeneralLinearGroup(F field, int n) {
    this.field = field;
    this.n = n;
    this.inversion = new MatrixInversion<>(field);
    this.multiplication = new MatrixMultiplication<>(field);
  }

  @Override
  public Matrix<E> invert(Matrix<E> a) {
    return inversion.apply(a);
  }

  @Override
  public Matrix<E> getIdentity() {
    return Matrix.eye(n, field);
  }

  @Override
  public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
    return multiplication.apply(a, b);
  }

  @Override
  public String toString(Matrix<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(Matrix<E> a, Matrix<E> b) {
    return a.equals(b, field::equals);
  }

  public String toString() {
    return "GL" + StringUtils.subscript(Integer.toString(n)) + "(" + field.toString() + ")";
  }
}
