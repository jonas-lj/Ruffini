package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassForm;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import dk.jonaslindstrom.math.algebra.elements.Fraction;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.math.BigInteger;

/** Copmute the WeilPairing of two points on an elliptic curve. */
public class WeilPairing<E> {

  private final WeierstrassForm<E> curve;
  private final Field<E> field;

  public WeilPairing(WeierstrassForm<E> curve) {
    this.curve = curve;
    this.field = curve.getField();
  }

  public E apply(ECPoint<E> P, ECPoint<E> Q, ECPoint<E> S, int m) {
    return apply(P, Q, S, BigInteger.valueOf(m));
  }

    /** Assume S is not in {O, P, -Q, P-Q}. */
  public E apply(ECPoint<E> P, ECPoint<E> Q, ECPoint<E> S, BigInteger m) {

    MillersAlgorithm<E> millersAlgorithm = new MillersAlgorithm<>(curve);

    E fPQS = millersAlgorithm.apply(P, curve.add(Q,S), m);
    E fPS = millersAlgorithm.apply(P, S, m);

    E fQPS = millersAlgorithm.apply(Q, curve.subtract(P,S), m);
    E fQS = millersAlgorithm.apply(Q, curve.negate(S), m);

    E r1 = field.divide(fPQS, fPS);
    E r2 = field.divide(fQS, fQPS);

    return field.multiply(r1, r2);
  }

}
