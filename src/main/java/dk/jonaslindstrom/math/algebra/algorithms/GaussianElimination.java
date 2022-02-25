package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.MutableMatrix;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class GaussianElimination<E> implements UnaryOperator<Matrix<E>> {

  private final Field<E> field;

  public GaussianElimination(Field<E> field) {
    this.field = field;
  }

  private void interchangeRows(MutableMatrix<E> matrix, int i, int j) {
    IntStream.range(0, matrix.getWidth()).parallel().forEach(k -> {
      E tmp = matrix.get(i, k);
      matrix.set(i, k, matrix.get(j, k));
      matrix.set(j, k, tmp);
    });
  }

  private void addMultipleOfRow(MutableMatrix<E> matrix, int from, int to, E multiple) {
    IntStream.range(0, matrix.getWidth()).parallel().forEach(k -> matrix.set(to, k,
        field.add(matrix.get(to, k), field.multiply(matrix.get(from, k), multiple))));
  }

  private void subtractMultipleOfRow(MutableMatrix<E> matrix, int from, int to, E multiple) {
    addMultipleOfRow(matrix, from, to, field.negate(multiple));
  }

  private void scaleRow(MutableMatrix<E> matrix, int row, E multiple) {
    IntStream.range(0, matrix.getWidth()).parallel().forEach(k ->
        matrix.set(row, k, field.multiply(matrix.get(row, k), multiple)));
  }

  @Override
  public Matrix<E> apply(Matrix<E> a) {
    int lead = 0;
    MutableMatrix<E> matrix = a.mutable();
    for (int j = 0; j < matrix.getWidth(); j++) {
      for (int i = lead; i < matrix.getHeight(); i++) {
        if (!field.isZero(matrix.get(i, j))) {
          if (i > lead) {
            interchangeRows(matrix, i, lead);
          }
          scaleRow(matrix, lead, field.invert(matrix.get(lead, j)));
          for (int k = 0; k < matrix.getHeight(); k++) {
            if (k == lead) {
              continue;
            }
            subtractMultipleOfRow(matrix, lead, k, matrix.get(k, j));
          }
          lead++;
        }
      }
    }
    return matrix;
  }

}
