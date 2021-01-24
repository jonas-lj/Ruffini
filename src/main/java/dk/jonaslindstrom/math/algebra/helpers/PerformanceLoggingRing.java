package dk.jonaslindstrom.math.algebra.helpers;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;

/**
 * Wrapper for the ring class which logs the number of operations performed in this ring.
 */
public class PerformanceLoggingRing<E> implements Ring<E> {

  private final Ring<E> ring;
  private int multiplications, additions, negations, equalities;

  public PerformanceLoggingRing(Ring<E> ring) {
    this.ring = ring;
  }

  @Override
  public E negate(E a) {
    synchronized (this) {
      negations++;
    }
    return ring.negate(a);
  }

  @Override
  public E add(E a, E b) {
    synchronized (this) {
      additions++;
    }
    return ring.add(a, b);
  }

  @Override
  public E getZero() {
    return ring.getZero();
  }

  @Override
  public E getIdentity() {
    return ring.getIdentity();
  }

  @Override
  public E multiply(E a, E b) {
    synchronized (this) {
      multiplications++;
    }
    return ring.multiply(a, b);
  }

  @Override
  public String toString(E a) {
    return ring.toString(a);
  }

  @Override
  public boolean equals(E a, E b) {
    synchronized (this) {
      equalities++;
    }
    return ring.equals(a, b);
  }

  public int getMultiplications() {
    return multiplications;
  }

  public int getAdditions() {
    return additions;
  }

  public int getNegations() {
    return negations;
  }

  public int getEqualities() {
    return equalities;
  }

  public String toString() {
    return "mults = " + multiplications + "\nadds  = " + additions + "\nneg   = " + negations
        + "\neqs   = " + equalities;
  }

  public void reset() {
    multiplications = 0;
    additions = 0;
    negations = 0;
    equalities = 0;
  }
}
