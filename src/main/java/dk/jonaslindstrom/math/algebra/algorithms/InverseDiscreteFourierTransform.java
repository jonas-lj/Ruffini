package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.vector.ConstructiveVector;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.function.UnaryOperator;

/**
 * Compute the Inverse Discrete Fourier Transform over a ring, assuming that <i>n</i> has an inverse
 * over the ring.
 */
public class InverseDiscreteFourierTransform<E> implements UnaryOperator<Vector<E>> {

  private final Ring<E> ring;
  private final int n;
  private final E nInverse;
  private final DiscreteFourierTransform<E> dft;

  public InverseDiscreteFourierTransform(Ring<E> ring, E nThPrincipalRootOfUnity, int n,
      E inverseOfN) {
    this.ring = ring;
    this.n = n;
    this.nInverse = inverseOfN;
    this.dft = new DiscreteFourierTransform<>(ring, nThPrincipalRootOfUnity, n);
  }

  public InverseDiscreteFourierTransform(Field<E> field, E nThPrincipalRootOfUnity, int n) {
    this(field, nThPrincipalRootOfUnity, n,
        field.invert(new IntegerRingEmbedding<>(field).apply(n)));
  }

  @Override
  public Vector<E> apply(Vector<E> x) {
    Vector<E> y = new ConstructiveVector<>(n, i -> x.get((n - i) % n));
    Vector<E> yHat = dft.apply(y);
    return yHat.map(e -> ring.multiply(e, nInverse));
  }

}
