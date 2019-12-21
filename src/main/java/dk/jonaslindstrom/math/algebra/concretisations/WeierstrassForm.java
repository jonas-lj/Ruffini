package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.math.algebra.algorithms.Multiply;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;

public class WeierstrassForm<E> implements EllipticCurve<E> {

  private final E a, b;
  private final Field<E> field;

  /**
   * Curve on Weierstrass form. Field should have characteristics not 2 nor 3.
   * 
   * @param field
   * @param a
   * @param b
   */
  public WeierstrassForm(Field<E> field, E a, E b) {
    this.field = field;
    this.a = a;
    this.b = b;
    assert(!field.equals(discriminant(), field.getZero()));
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
    
    if (field.equals(p.x, q.x)) {
      if (field.equals(p.y, field.negate(q.y))) {
        return ECPoint.pointAtInfinity();
      }
      IntegerRingEmbedding<E> embedding = new IntegerRingEmbedding<>(field);
      E s = field.divide(field.add(field.multiply(embedding.apply(3), field.multiply(p.x, p.x)), a),
          field.multiply(embedding.apply(2), p.y));
      E x = field.subtract(field.multiply(s, s), field.multiply(embedding.apply(2), p.x));
      E y = field.add(p.y, field.multiply(s, field.subtract(x, p.x)));
      y = field.negate(y);
      return new ECPoint<>(x, y);
    }
    
    E s = field.divide(field.subtract(p.y, q.y), field.subtract(p.x, q.x));
    E x = field.subtract(field.multiply(s, s), field.add(p.x, p.y));
    E y = field.add(p.y, field.multiply(s, field.subtract(x, p.x)));
    y = field.negate(y);
    
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
    return "E: y² = " + rhs.toString("x");
  }

}
