package dk.jonaslindstrom.math.algebra.elements.matrix;

import dk.jonaslindstrom.math.functional.IntBinaryFunction;

public class MutableMatrix<E> extends ConcreteMatrix<E> {

  public MutableMatrix(int m, int n, E defaultValue) {
    super(m, n, (i, j) -> defaultValue);
  }

  public MutableMatrix(int m, int n, IntBinaryFunction<E> populator) {
    super(m, n, populator);
  }

  MutableMatrix(Matrix<E> matrix) {
    super(matrix.getHeight(), matrix.getWidth(), matrix::get);
  }

  MutableMatrix(ConcreteMatrix<E> matrix) {
    super(matrix.rows);
  }

  @Override
  public void set(int i, int j, E value) {
    super.set(i, j, value);
  }

}
