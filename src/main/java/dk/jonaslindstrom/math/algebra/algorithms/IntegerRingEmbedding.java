package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.abstractions.SemiRing;
import java.util.function.IntFunction;

/**
 * This function computes the canonical embedding of the integers into any ring by mapping an
 * integer <i>n</i> to the identity element added to itself <i>n</i> times.
 */
public class IntegerRingEmbedding<E> implements IntFunction<E> {

  private final SemiRing<E> ring;

  public IntegerRingEmbedding(SemiRing<E> ring) {
    this.ring = ring;
  }

  @Override
  public E apply(int a) {

    if (a < 0) {
      if (ring instanceof Ring) {
        return ((Ring<E>) this.ring).negate(apply(-a));
      }
      throw new IllegalArgumentException("Negative integers are only allowed for rings.");
    } else if (a == 0) {
      return ring.getZero();
    } else if (a == 1) {
      return ring.getIdentity();
    }

    E b = ring.getZero();
    E p = ring.getIdentity();
    E two = ring.add(ring.getIdentity(), ring.getIdentity());

    while (a > 0) {
      if ((a & 1) != 0) {
        // a odd
        b = ring.add(b, p);
      }
      a = a >> 1;

      if (a > 0) {
        p = ring.multiply(two, p);
      }
    }
    return b;
  }

}
