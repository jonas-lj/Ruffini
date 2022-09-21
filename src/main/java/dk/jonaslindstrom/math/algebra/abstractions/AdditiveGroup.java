package dk.jonaslindstrom.math.algebra.abstractions;

import dk.jonaslindstrom.math.algebra.algorithms.BigMultiply;

import java.math.BigInteger;

public interface AdditiveGroup<E> extends CommutativeMonoid<E> {

  /** Return <i>-a</i>. */
  E negate(E a);

  /** Compute <i>a-b</i>. */
  default E subtract(E a, E b) {
    return add(a, negate(b));
  }

  default boolean isZero(E a) {
    return this.equals(a, this.getZero());
  }

  /**
   * Return <i>e</i> added to it self <i>n</i> times in this monoid
   */
  default E scale(BigInteger n, E e) {
    return new BigMultiply<>(this).apply(n, e);
  }
}
