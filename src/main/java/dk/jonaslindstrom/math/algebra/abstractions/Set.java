package dk.jonaslindstrom.math.algebra.abstractions;

/**
 * 
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public interface Set<E> {

  /**
   * Return a human readable string representation of an element in this set.
   * 
   * @param a
   * @return
   */
  public String toString(E a);

  /**
   * Return <code>true</code> if and only if <i>a = b</i> as elements of this set.
   * 
   * @param a
   * @param b
   * @return
   */
  public boolean equals(E a, E b);

}
