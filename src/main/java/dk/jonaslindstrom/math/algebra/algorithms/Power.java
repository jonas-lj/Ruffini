package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import java.util.function.BiFunction;

/**
 * Compute <i>a<sup>e</sup></i>.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <E>
 */
public class Power<E> implements BiFunction<E, Integer, E> {

  private Ring<E> ring;

  public Power(Ring<E> ring) {
    this.ring = ring;
  }

  @Override
  public E apply(E a, Integer e) {
    if (e == 0) {
      return ring.getIdentity();
    } else if (e == 1) {
      return a;
    }

    int f = e;
    if (f % 2 == 1) {
      return ring.multiply(a, apply(ring.multiply(a, a), (e - 1) / 2));
    } else {
      return apply(ring.multiply(a, a), e / 2);
    }
  }

}
