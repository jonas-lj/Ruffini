package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.util.Pair;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 * 
 * @author jonas
 *
 * @param <E>
 */
public class PolynomialRing<E> extends PolynomialRingOverRing<E>
    implements EuclideanDomain<Polynomial<E>> {

  private Field<E> field;

  public PolynomialRing(Field<E> field) {
    super(field);
    this.field = field;
  }

  public Field<E> getBaseField() {
    return field;
  }

  @Override
  public Pair<Polynomial<E>, Polynomial<E>> divisionWithRemainder(Polynomial<E> a,
      Polynomial<E> b) {
    return super.divisionWithRemainder(a, b, field.invert(b.getCoefficient(b.degree())));
  }

  @Override
  public Integer norm(Polynomial<E> a) {
    return a.degree();
  }

  @Override
  public String toString() {
    return field.toString() + "(x)";
  }

}
