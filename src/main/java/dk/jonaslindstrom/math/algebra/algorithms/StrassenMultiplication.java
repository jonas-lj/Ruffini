package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.helpers.MultiOperator;
import dk.jonaslindstrom.math.algebra.helpers.NullSafeRing;
import dk.jonaslindstrom.math.util.MathUtils;
import java.util.function.BinaryOperator;

/**
 * This function computes the product of two square matrices using the Strassen algorithm.
 */
public class StrassenMultiplication<E> implements BinaryOperator<Matrix<E>> {

  private final Ring<E> ring;
  private final int bound;

  public StrassenMultiplication(Ring<E> ring, int bound) {
    this.ring = ring;
    this.bound = bound;
  }

  public StrassenMultiplication(Ring<E> ring) {
    this(ring, 1);
  }

  @Override
  public Matrix<E> apply(Matrix<E> a, Matrix<E> b) {
    assert (a.isSquare() && b.isSquare());
    assert (a.getHeight() == b.getHeight());

    int n = a.getHeight();

    if (n == 1) {
      return Matrix.of(1, 1, new NullSafeRing<>(ring).multiply(a.get(0, 0), b.get(0, 0)));
    }

    if (n <= bound) {
      return new MatrixMultiplication<>(ring).apply(a, b);
    }

    int N = MathUtils.nextPowerOfTwo(n);
    if (N > n) {
      return apply(a.view().extendTo(N, N, null), b.view().extendTo(N, N, null)).submatrix(0, n, 0, n);
    }

    int m = n / 2;

    Matrix<Matrix<E>> A = Matrix
        .of(2, 2, (i, j) -> a.view().submatrix(i * m, (i + 1) * m, j * m, (j + 1) * m));

    Matrix<Matrix<E>> B = Matrix
        .of(2, 2, (i, j) -> b.view().submatrix(i * m, (i + 1) * m, j * m, (j + 1) * m));

    Matrix<E> m1 = apply(plus(A.get(0, 0), A.get(1, 1)), plus(B.get(0, 0), B.get(1, 1)));
    Matrix<E> m2 = apply(plus(A.get(1, 0), A.get(1, 1)), B.get(0, 0));
    Matrix<E> m3 = apply(A.get(0, 0), plus(B.get(0, 1), B.get(1, 1).view().map(this::minus)));
    Matrix<E> m4 = apply(A.get(1, 1), plus(B.get(1, 0), B.get(0, 0).view().map(this::minus)));
    Matrix<E> m5 = apply(plus(A.get(0, 0), A.get(0, 1)), B.get(1, 1));
    Matrix<E> m6 = apply(plus(A.get(1, 0), A.get(0, 0).view().map(this::minus)),
        plus(B.get(0, 0), B.get(0, 1)));
    Matrix<E> m7 = apply(plus(A.get(0, 1), A.get(1, 1).view().map(this::minus)),
        plus(B.get(1, 0), B.get(1, 1)));

    MultiOperator<Matrix<E>> sum = new MultiOperator<>(this::plus);

    Matrix<Matrix<E>> C = Matrix.of(2, sum.apply(m1, m4, m5.view().map(this::minus), m7),
        plus(m3, m5), plus(m2, m4), sum.apply(m1, m2.view().map(this::minus), m3, m6));

    return Matrix.fromBlocks(C);
  }

  private E minus(E e) {
    if (e == null) {
      return null;
    }
    return ring.negate(e);
  }

  private Matrix<E> plus(Matrix<E> a, Matrix<E> b) {
    assert (a.getHeight() == b.getHeight() && a.getWidth() == b.getWidth());

    return Matrix.of(a.getHeight(), a.getWidth(), (i, j) -> {
      E aij = a.get(i, j);
      E bij = b.get(i, j);

      if (aij == null) {
        return bij;
      }

      if (bij == null) {
        return aij;
      }

      return ring.add(aij, bij);
    });
  }

}
