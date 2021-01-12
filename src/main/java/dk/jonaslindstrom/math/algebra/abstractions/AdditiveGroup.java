package dk.jonaslindstrom.math.algebra.abstractions;

public interface AdditiveGroup<E> extends CommutativeMonoid<E> {

  /**
   * Return <i>-a</i>
   */
  E negate(E a);

  /**
   * Compute <i>a-b</i>.
   */
  default E subtract(E a, E b) {
    return add(a, negate(b));
  }

}
