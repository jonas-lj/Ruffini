package dk.jonaslindstrom.math.algebra.abstractions;

import dk.jonaslindstrom.math.util.Pair;

public interface EuclideanDomain<E> extends Ring<E> {

  /**
   * Returns a pair <i>(q, r)</i> such that <i>a = qb + r</i> and <i>0 ≤ f(r) < f(b)</i> where
   * <i>f</i> is the Euclidean function (see {@link #norm(E)} for this domain.
   */
  Pair<E, E> divisionWithRemainder(E a, E b);

  /**
   * The euclidean function is a multiplicative map that maps elements of the domain to the integers
   * and is used in the division of {@link #divisionWithRemainder(E, E)}.
   */
  Integer norm(E a);

}
