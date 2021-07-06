package dk.jonaslindstrom.math.algebra.elements.matrix;

import dk.jonaslindstrom.math.functional.IntBinaryFunction;
import java.util.function.Function;

class MatrixView<E> extends BaseMatrix<E> {

  private final int m;
  private final int n;
  private final IntBinaryFunction<E> entries;

  public MatrixView(int m, int n, IntBinaryFunction<E> entries) {
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
    return new MatrixView<>(m - 1, n - 1,
        (a, b) -> get(a >= i ? a + 1 : a, b >= j ? b + 1 : b));
  }

  @Override
  public Matrix<E> transpose() {
    return new MatrixView<>(n, m, (i, j) -> get(j, i));
  }

  @Override
  public Matrix<E> extendTo(int m, int n, E padding) {
    return new MatrixView<>(m, n,
        (i, j) -> (i < this.m && j < this.n) ? this.get(i, j) : padding);
  }

  @Override
  public Matrix<E> submatrix(int[] rows, int[] columns) {
    return new MatrixView<>(rows.length, columns.length,
        (i, j) -> this.get(rows[i], columns[j]));
  }

  @Override
  public Matrix<E> submatrix(int r0, int r1, int c0, int c1) {
    return new MatrixView<>(r1 - r0, c1 - c0, (i, j) -> this.get(i + r0, j + c0));
  }

  @Override
  public <F> Matrix<F> map(Function<E, F> f) {
    return new MatrixView<>(getHeight(), getWidth(), (i, j) -> f.apply(get(i, j)));
  }

  @Override
  public Matrix<E> view() {
    return this;
  }

}
