package dk.jonaslindstrom.math.algebra.abstractions;

public interface Monoid<E> extends Semigroup<E> {

  /**
   * Return the identity element of this monoid.
   */
  E getIdentity();

}
