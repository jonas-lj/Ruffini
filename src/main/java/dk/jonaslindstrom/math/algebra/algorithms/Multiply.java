package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import java.util.function.BiFunction;

/**
 * Compute <i>e a</i> for and integer <i>e</i> and an element from an additive group, <i>a</i>.
 */
public class Multiply<E> implements BiFunction<Integer, E, E> {

  private final AdditiveGroup<E> group;

  public Multiply(AdditiveGroup<E> group) {
    this.group = group;
  }

  @Override
  public E apply(Integer e, E a) {
    if (e == 0) {
      return group.getZero();
    } else if (e == 1) {
      return a;
    }

    int f = e;
    if (f % 2 == 1) {
      return group.add(a, apply((e - 1) / 2, group.add(a, a)));
    } else {
      return apply(e / 2, group.add(a, a));
    }
  }

}
