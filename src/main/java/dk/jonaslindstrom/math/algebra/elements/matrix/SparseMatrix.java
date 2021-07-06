package dk.jonaslindstrom.math.algebra.elements.matrix;

import dk.jonaslindstrom.math.util.MatrixIndex;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SparseMatrix<E> extends MatrixView<E> {

  private SparseMatrix(int m, int n, Map<MatrixIndex, E> entries, E zero) {
    super(m, n, (i, j) -> {
      E e = entries.get(MatrixIndex.of(i, j));
      return Objects.nonNull(e) ? e : zero;
    });
  }

  public static class Builder<F> {

    private final HashMap<MatrixIndex, F> entries = new HashMap<>();
    private final int m;
    private final int n;
    private final F zero;

    public Builder(int m, int n, F zero) {
      this.m = m;
      this.n = n;
      this.zero = zero;
    }

    public Builder<F> add(int i, int j, F value) {
      assert (i < m && j < n);
      entries.put(MatrixIndex.of(i, j), value);
      return this;
    }

    public Matrix<F> build() {
      return new SparseMatrix<>(m, n, entries, zero);
    }
  }

}
