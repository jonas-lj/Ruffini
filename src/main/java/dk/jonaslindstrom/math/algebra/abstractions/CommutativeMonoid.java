package dk.jonaslindstrom.math.algebra.abstractions;

public interface CommutativeMonoid<E> extends Set<E> {

  /**
   * Return the result of <i>a+b</i>.
   */
  E add(E a, E b);

  /**
   * Return the result of <i>a+b+c</i>.
   */
  default E add(E a, E b, E c) {
    return add(add(a,b), c);
  }

  /**
   * Return the result of <i>a+b+c+d</i>.
   */
  default E add(E a, E b, E c, E d) {
    return add(add(a, b), add(c, d));
  }

  /**
   * Return the zero element.
   */
  E getZero();

}
