package dk.jonaslindstrom.math.algebra.concretisations;

import java.util.List;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.EdwardsPoint;

public class EdwardsCurve<E> implements EllipticCurve<EdwardsPoint<E>> {

  private final Field<E> field;
  private final E d;

  public EdwardsCurve(Field<E> field, E d) {
    this.field = field;
    this.d = d;
  }

  @Override
  public String toString(EdwardsPoint<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(EdwardsPoint<E> a, EdwardsPoint<E> b) {
    return field.equals(a.x, b.x) && field.equals(a.y, b.y);
  }

  private E multiply(List<E> factors) {
    if (factors.isEmpty()) {
      return field.getIdentity();
    }
    E p = factors.get(0);
    for (int i = 1; i < factors.size(); i++) {
      p = field.multiply(p, factors.get(i));
    }
    return p;
  }

  @Override
  public EdwardsPoint<E> add(EdwardsPoint<E> a, EdwardsPoint<E> b) {

    E n1 = field.add(field.multiply(a.x, b.y), field.multiply(a.y, b.x));
    E n2 = field.subtract(field.multiply(a.y, b.y), field.multiply(a.x, b.x));

    E p = multiply(List.of(d, a.x, b.x, a.y, b.y));
    E d1 = field.add(field.getIdentity(), p);
    E d2 = field.subtract(field.getIdentity(), p);

    return new EdwardsPoint<>(field.divide(n1, d1), field.divide(n2, d2));
  }

  @Override
  public EdwardsPoint<E> negate(EdwardsPoint<E> a) {
    return new EdwardsPoint<>(field.negate(a.x), a.y);
  }

  @Override
  public EdwardsPoint<E> getZero() {
    return new EdwardsPoint<>(field.getZero(), field.getIdentity());
  }

  private boolean isZero(EdwardsPoint<E> a) {
    return field.equals(a.x, field.getZero()) && field.equals(a.y, field.getIdentity());
  }

}
