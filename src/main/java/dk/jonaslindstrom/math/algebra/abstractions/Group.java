package dk.jonaslindstrom.math.algebra.abstractions;

/**
 * 
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public interface Group<E> extends Monoid<E> {

  /**
   * Return the inverse <i>a<sup>-1</sup></i>.
   * 
   * @param a
   * @return
   */
  public E invert(E a);

  public default E divide(E a, E b) {
    return multiply(a, invert(b));
  }

}
