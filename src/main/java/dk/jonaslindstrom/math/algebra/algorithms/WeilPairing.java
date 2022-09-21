package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassCurve;
import dk.jonaslindstrom.math.algebra.elements.curves.AffinePoint;
import java.math.BigInteger;

/** Copmute the WeilPairing of two points on an elliptic curve. */
public class WeilPairing<E> {

  private final WeierstrassCurve<E> curve;
  private final Field<E> field;

  public WeilPairing(WeierstrassCurve<E> curve) {
    this.curve = curve;
    this.field = curve.getField();
  }

  public E apply(AffinePoint<E> P, AffinePoint<E> Q, AffinePoint<E> S, int m) {
    return apply(P, Q, S, BigInteger.valueOf(m));
  }

  /** Assume S is not in {O, P, -Q, P-Q}. */
  public E apply(AffinePoint<E> P, AffinePoint<E> Q, AffinePoint<E> S, BigInteger m) {

    MillersAlgorithm<E> millersAlgorithm = new MillersAlgorithm<>(curve);

    E fPQS = millersAlgorithm.apply(P, curve.add(Q,S), m);
    E fPS = millersAlgorithm.apply(P, S, m);

    E fQPS = millersAlgorithm.apply(Q, curve.subtract(P,S), m);
    E fQS = millersAlgorithm.apply(Q, curve.negate(S), m);

    return field.divide(field.multiply(fPQS, fQS), field.multiply(fPS, fQPS));
  }

}
