package dk.jonaslindstrom.math.algebra.elements.matrix;

import dk.jonaslindstrom.math.functional.IntBinaryFunction;
import java.util.function.Function;

class ConstructiveMatrix<E> extends BaseMatrix<E> {

  private int m;
  private int n;
  private IntBinaryFunction<E> entries;

  public ConstructiveMatrix(int m, int n, IntBinaryFunction<E> entries) {
    this.m = m;
    this.n = n;
    this.entries = entries;
  }

  @Override
  public E get(int i, int j) {
    return entries.apply(i, j);
  }

  @Override
  public int getHeight() {
    return m;
  }

  @Override
  public int getWidth() {
    return n;
  }

  @Override
  public Matrix<E> minor(int i, int j) {
    return new ConstructiveMatrix<>(m - 1, n - 1,
        (a, b) -> get(a >= i ? a + 1 : a, b >= j ? b + 1 : b));
  }

  @Override
  public Matrix<E> transpose() {
    return new ConstructiveMatrix<>(n, m, (i, j) -> get(j, i));
  }

  @Override
  public Matrix<E> extend(int m, int n, E padding) {
    return new ConstructiveMatrix<>(m, n,
        (i, j) -> (i < this.m && j < this.n) ? this.get(i, j) : padding);
  }

  @Override
  public Matrix<E> submatrix(int[] rows, int[] columns) {
    return new ConstructiveMatrix<>(rows.length, columns.length,
        (i, j) -> this.get(rows[i], columns[j]));
  }

  @Override
  public Matrix<E> submatrix(int r0, int r1, int c0, int c1) {
    return new ConstructiveMatrix<>(r1 - r0, c1 - c0, (i, j) -> this.get(i + r0, j + c0));
  }

  @Override
  public <F> Matrix<F> forEach(Function<E, F> f) {
    return new ConstructiveMatrix<>(getHeight(), getWidth(), (i, j) -> f.apply(get(i, j)));
  }

  @Override
  public Matrix<E> view() {
    return this;
  }

}
