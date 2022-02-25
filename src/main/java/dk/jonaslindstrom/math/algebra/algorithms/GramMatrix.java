package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.MutableMatrix;
import dk.jonaslindstrom.math.algebra.elements.vector.ConcreteVector;
import dk.jonaslindstrom.math.algebra.helpers.NullSafeRing;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * Compute the Gram matrix for a given matrix.
 */
public class GramMatrix<E> implements UnaryOperator<Matrix<E>> {

  private final Ring<E> ring;

  public GramMatrix(Ring<E> ring) {
    this.ring = ring;
  }

  @Override
  public Matrix<E> apply(Matrix<E> a) {

    if (a.isSquare()) {
      MutableMatrix<E> C = new MutableMatrix<>(a.getHeight(), a.getHeight(), (i,j) -> null);
      NullSafeRing<E> ops = new NullSafeRing<>(ring);
      DotProduct<E> dot = new DotProduct<>(ops);
      for (int i = 0; i < a.getHeight(); i++) {
        for (int j = 0; j <= i; j++) {
          E c = dot.apply(a.getRow(i), a.getRow(j));
          C.set(i, j, c);
          if (i != j) {
            C.set(j, i, c);
          }
        }
      }
      return C;
    }

    int m = a.getHeight();
    int n = a.getWidth();

    int q = m / n;
    int r = m - q * n;

    int k = r > 0 ? q + 1 : q;

    ConcreteVector<Matrix<E>> A = new ConcreteVector<>(k,
        i -> a.submatrix(i * n, Math.min((i + 1) * n, m), 0, n).extendTo(n, n, null));

    BinaryOperator<Matrix<E>> mult = new StrassenMultiplication<>(ring);

    MutableMatrix<Matrix<E>> C = new MutableMatrix<>(k, k, (i,j) -> null);
    for (int i = 0; i < k; i++) {
      for (int j = 0; j <= i; j++) {
        if (i == j) {
          C.set(i, i, apply(A.get(i)));
        } else {
          Matrix<E> cij = mult.apply(A.get(i), A.get(j).transpose());
          C.set(i, j, cij);
          C.set(j, i, cij.transpose());
        }
      }
    }

    return Matrix.fromBlocks(C).submatrix(0, m, 0, m);
  }

}
