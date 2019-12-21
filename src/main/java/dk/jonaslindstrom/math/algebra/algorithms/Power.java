package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Monoid;

import java.util.function.BiFunction;

/**
 * Compute <i>a<sup>e</sup></i>.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class Power<E> implements BiFunction<E, Integer, E> {

  private Monoid<E> group;

  public Power(Monoid<E> group) {
    this.group = group;
  }

  @Override
  public E apply(E a, Integer e) {
    if (e == 0) {
      return group.getIdentity();
    } else if (e == 1) {
      return a;
    }

    int f = e;
    if (f % 2 == 1) {
      return group.multiply(a, apply(group.multiply(a, a), (e - 1) / 2));
    } else {
      return apply(group.multiply(a, a), e / 2);
    }
  }

}
