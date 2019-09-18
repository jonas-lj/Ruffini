package dk.jonaslindstrom.math.algebra.abstractions;

public interface AdditiveGroup<E> extends Set<E> {

  /**
   * Return the result of <i>a+b</i> in this group.
   * 
   * @param a
   * @param b
   * @return <i>a+b</i>
   */
  public E add(E a, E b);

  /**
   * Return <i>-a</i>
   * 
   * @param a
   * @return <i>-a</i>
   */
  public E negate(E a);

  /**
   * Return the zero element in this group.
   * 
   * @return <i>0</i>
   */
  public E getZero();
  
  public default E subtract(E a, E b) {
    return add(a, negate(b));
  }

}
