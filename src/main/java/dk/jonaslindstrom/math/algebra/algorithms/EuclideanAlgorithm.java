package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.Triple;

public class EuclideanAlgorithm<E> {

  private EuclideanDomain<E> ring;

  public EuclideanAlgorithm(EuclideanDomain<E> domain) {
    this.ring = domain;
  }
  
  /**
   * Calculate the greatest common divisor <i>d</i> of <i>a</i> and <i>b</i>, and the coefficients
   * <i>x,y</i> of the Bezout identity <i>ax + by = d</i> .
   * 
   * @param ring
   * @param a
   * @param b
   * @return The triple <i>(d, x, y)</i>.
   */
  public Triple<E, E, E> extendedGcd(E a, E b) {
    E s = ring.getZero();
    E oldS = ring.getIdentity();
    E t = ring.getIdentity();
    E oldT = ring.getZero();
    E r = b;
    E oldR = a;

    while (!ring.equals(r, ring.getZero())) {
      Pair<E, E> division = ring.divisionWithRemainder(oldR, r);
      E q = division.getFirst();

      oldR = r;
      r = division.getSecond();

      E tmpS = s;
      s = ring.add(oldS, ring.negate(ring.multiply(q, tmpS)));
      oldS = tmpS;

      E tmpT = t;
      t = ring.add(oldT, ring.negate(ring.multiply(q, tmpT)));
      oldT = tmpT;

    }
    return new Triple<>(oldR, oldS, oldT);
  }

//  /**
//   * Assuming that <i>a</i> and <i>m</i> are co-prime in the given ring, this algorithm returns the
//   * multiplicative inverse <i>a<sup>-1</sup></i> of <i>a</i> modulo <i>m</i>.
//   * 
//   * @param ring
//   * @param a
//   * @param m
//   * @return
//   */
//  public E invertModM(E a, E m) {
//    Triple<E, E, E> gcd = extendedGcd(a, m);
//
//    if (!ring.equals(gcd.getFirst(), ring.getIdentity())) {
//      throw new ArithmeticException(
//          "Failed to invert element " + a + " since it's not coprime with " + m);
//    }
//
//    return gcd.getSecond();
//  }

}
