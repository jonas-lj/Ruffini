package dk.jonaslindstrom.math.algebra.elements;

import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.SparseMatrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.SparseMatrix.Builder;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class Permutation implements UnaryOperator<Integer> {

  private final int[] p;

  public Permutation(Integer n) {
    // Identity permutation
    this.p = new int[n];
    for (int i = 0; i < n; i++) {
      p[i] = i;
    }
  }

  public Permutation(int n, int[] cycle) {
    this.p = new int[n];
    Arrays.fill(p, -1);
    for (int i = 0; i < cycle.length - 1; i++) {
      p[cycle[i]] = cycle[i + 1];
    }
    p[cycle[cycle.length - 1]] = cycle[0];

    for (int i = 0; i < n; i++) {
      if (p[i] < 0) {
        p[i] = i;
      }
    }
  }

  public Permutation(int... permutation) {
    this.p = permutation;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("⎛ ");
    for (int i = 0; i < p.length; i++) {
      sb.append(i + " ");
    }
    sb.append("⎞\n⎝ ");
    for (int i = 0; i < p.length; i++) {
      sb.append(apply(i));
      sb.append(" ");
    }
    sb.append("⎠");
    return sb.toString();
  }

  @Override
  public Integer apply(Integer x) {
    if (x < 0 || x >= p.length) {
      throw new IllegalArgumentException();
    }
    return p[x];
  }

  public Matrix<Integer> getMatrixRepresentation() {
    SparseMatrix.Builder<Integer> builder = new Builder<>(p.length, p.length, 0);
    for (int i = 0; i < p.length; i++) {
      builder.add(i, apply(i), 1);
    }
    return builder.build();
  }

}
