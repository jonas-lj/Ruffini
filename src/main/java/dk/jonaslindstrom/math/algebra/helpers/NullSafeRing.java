package dk.jonaslindstrom.math.algebra.helpers;

import java.util.Objects;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;

/** This class wraps a ring but operations will treat null operands as if they were zero. */
public class NullSafeRing<E> implements Ring<E> {

  private final Ring<E> ring;

  public NullSafeRing(Ring<E> ring) {
    this.ring = ring;
  }

  public E multiply(E a, E b) {
    if (a == null || b == null) {
      return null;
    }
    return ring.multiply(a, b);
  }

  public E add(E a, E b) {
    if (a == null && b != null) {
      return b;
    } else if (a != null && b == null) {
      return a;
    } else if (a == null) {
      return ring.getZero();
    } else {
      return ring.add(a, b);
    }
  }

  public E negate(E a) {
    if (a == null) {
      return null;
    }
    return ring.negate(a);
  }

  @Override
  public E getIdentity() {
    return ring.getIdentity();
  }

  @Override
  public String toString(E a) {
    if (Objects.isNull(a)) {
      return ring.toString(ring.getZero());
    }
    return ring.toString(a);
  }

  @Override
  public boolean equals(E a, E b) {
    if (Objects.isNull(a)) {
      if (Objects.isNull(b)) {
        return true;
      } else {
        return ring.equals(b, ring.getZero());
      }
    }

    if (Objects.isNull(b)) {
      return ring.equals(a, ring.getZero());
    }

    return ring.equals(a, b);
  }

  @Override
  public E getZero() {
    return ring.getZero();
  }

  @Override
  public int getCharacteristics() {
    return ring.getCharacteristics();
  }
}
