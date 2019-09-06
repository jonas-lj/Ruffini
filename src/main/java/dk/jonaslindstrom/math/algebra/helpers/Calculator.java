package dk.jonaslindstrom.math.algebra.helpers;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;

public class Calculator<E> {

  private Field<E> field;
  private Ring<E> ring;
  private AdditiveGroup<E> group;

  public Calculator(Field<E> field) {
    this.field = field;
    this.ring = field;
    this.group = field;
  }

  public Calculator(Ring<E> ring) {
    this.ring = ring;
    this.group = ring;
  }

  public Calculator(AdditiveGroup<E> group) {
    this.group = group;
  }

  @SafeVarargs
  public final E sum(E... terms) {
    E result = group.getZero();
    for (E term : terms) {
      result = group.add(result, term);
    }
    return result;
  }

  @SafeVarargs
  public final E mul(E... factors) {
    E result = ring.getIdentity();
    for (E factor : factors) {
      result = ring.multiply(result, factor);
    }
    return result;
  }

  @SafeVarargs
  public final E mul(int n, E... factors) {
    E result = ring.getZero();
    E a = mul(factors);
    for (int i = 0; i < n; i++) {
      result = ring.add(result, a);
    }
    return result;
  }

  public E sub(E a, E b) {
    return group.add(a, group.negate(b));
  }

  public E div(E n, E d) {
    if (field == null) {
      throw new UnsupportedOperationException();
    }
    return field.multiply(n, field.invert(d));
  }
  
  public E inv(E d) {
    if (field == null) {
      throw new UnsupportedOperationException();
    }
    return field.invert(d);
  }


  public E pow(E a, int n) {
    E result = ring.getIdentity();
    for (int i = 0; i < n; i++) {
      result = ring.multiply(result, a);
    }
    return result;
  }

  public E sq(E a) {
    return pow(a, 2);
  }

  /**
   * Return the negation <i>-a</i> of an element <i>a</i>.
   * 
   * @param a
   * @return
   */
  public E m(E a) {
    return group.negate(a);
  }
}
