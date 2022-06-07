package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.math.algebra.algorithms.Multiply;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;

public class WeierstrassForm<E> implements EllipticCurve<ECPoint<E>> {

  private final E a, b;

  public Field<E> getField() {
    return field;
  }

  private final Field<E> field;
  private final IntegerRingEmbedding<E> embedding;

  /**
   * Curve on Weierstrass form. Field should have characteristics not equal to 2 or 3.
   */
  public WeierstrassForm(Field<E> field, E a, E b) {
    this.field = field;
    this.a = a;
    this.b = b;
    assert (!field.equals(discriminant(), field.getZero()));
    this.embedding = new IntegerRingEmbedding<>(field);
  }

  public E discriminant() {
    Multiply<E> multiply = new Multiply<>(field);
    Power<E> power = new Power<>(field);

    return field.negate(multiply.apply(16,
        field.add(
            multiply.apply(4, power.apply(a, 3)),
            multiply.apply(27, field.multiply(b, b)))));
  }

  @Override
  public String toString(ECPoint<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(ECPoint<E> a, ECPoint<E> b) {
    if (a.isPointAtInfinity()) {
      return b.isPointAtInfinity();
    } else if (b.isPointAtInfinity()) {
      return a.isPointAtInfinity();
    }
    return field.equals(a.x, b.x) && field.equals(a.y, b.y);
  }

  @Override
  public ECPoint<E> add(ECPoint<E> p, ECPoint<E> q) {
    if (p.isPointAtInfinity()) {
      return q;
    }

    if (q.isPointAtInfinity()) {
      return p;
    }

    E s;
    if (field.equals(p.x, q.x)) {
      if (field.equals(p.y, field.negate(q.y))) {
        return ECPoint.pointAtInfinity();
      }
      s = field.divide(field.add(field.multiply(embedding.apply(3), p.x, p.x), a),
          field.add(p.y, p.y));
    } else {
      s = field.divide(field.subtract(q.y, p.y), field.subtract(q.x, p.x));
    }

    E x = field.subtract(field.multiply(s, s), field.add(p.x, q.x));
    E y = field.subtract(field.multiply(s, field.subtract(p.x, x)), p.y);
    return new ECPoint<>(x, y);
  }

  @Override
  public ECPoint<E> negate(ECPoint<E> p) {
    return new ECPoint<>(p.x, field.negate(p.y));
  }

  @Override
  public ECPoint<E> getZero() {
    return ECPoint.pointAtInfinity();
  }

  public String toString() {
    Polynomial<E> rhs = Polynomial.of(field, b, a, field.getZero(), field.getIdentity());
    return "E: y^2 = " + rhs.toString("x");
  }

  public E getA() {
    return a;
  }

  public E getB() {
    return b;
  }
}
