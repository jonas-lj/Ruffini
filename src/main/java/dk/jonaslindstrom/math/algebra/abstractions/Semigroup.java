package dk.jonaslindstrom.math.algebra.abstractions;

public interface Semigroup<E> extends Set<E> {

  /**
   * Return the result of the product <i>ab</i> in this group.
   * 
   * @param a
   * @param b
   * @return
   */
  public E multiply(E a, E b);

}
