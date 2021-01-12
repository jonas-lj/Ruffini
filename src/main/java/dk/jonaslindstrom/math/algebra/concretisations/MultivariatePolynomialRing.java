package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.algorithms.MultivariatePolynomialDivision;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.Objects;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial.Monomial;
import dk.jonaslindstrom.math.util.Pair;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 *
 * @author jonas
 *
 * @param <E>
 */
public class MultivariatePolynomialRing<E>
    implements EuclideanDomain<MultivariatePolynomial<E>> {

  private final Field<E> field;
  private final int variables;

  public MultivariatePolynomialRing(Field<E> field, int variables) {
    this.field = field;
    this.variables = variables;
  }

  public Field<E> getField() {
    return field;
  }

  @Override
  public String toString() {
    return field.toString() + "(x)";
  }

  @Override
  public MultivariatePolynomial<E> getIdentity() {
    return MultivariatePolynomial.constant(field.getIdentity(), variables);
  }

  @Override
  public MultivariatePolynomial<E> multiply(MultivariatePolynomial<E> a,
      MultivariatePolynomial<E> b) {
    return MultivariatePolynomial.multiply(a, b, field);
  }

  @Override
  public String toString(MultivariatePolynomial<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(MultivariatePolynomial<E> a, MultivariatePolynomial<E> b) {
    for (Pair<Monomial, E> ai : a.coefficients()) {
      E bi = b.getCoefficient(ai.first);
      if (Objects.isNull(bi)) {
        if (field.equals(ai.second, field.getZero())) {
          continue;
        } else {
          return false;
        }
      }

      if (!field.equals(ai.second, b.getCoefficient(ai.first))) {
        return false;
      }
    }

    for (Pair<Monomial, E> bi : b.coefficients()) {
      E ai = b.getCoefficient(bi.first);
      if (Objects.isNull(ai)) {
        if (field.equals(bi.second, field.getZero())) {
          continue;
        } else {
          return false;
        }
      }

      if (!field.equals(bi.second, a.getCoefficient(bi.first))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public MultivariatePolynomial<E> add(MultivariatePolynomial<E> a, MultivariatePolynomial<E> b) {
    return MultivariatePolynomial.add(a, b, field);
  }

  @Override
  public MultivariatePolynomial<E> negate(MultivariatePolynomial<E> a) {
    return a.mapCoefficients(field::negate);
  }

  @Override
  public MultivariatePolynomial<E> getZero() {
    return MultivariatePolynomial.constant(field.getZero(), variables);
  }

  @Override
  public Pair<MultivariatePolynomial<E>, MultivariatePolynomial<E>> divisionWithRemainder(
      MultivariatePolynomial<E> a, MultivariatePolynomial<E> b) {
    return null;
  }

  @Override
  public Integer norm(MultivariatePolynomial<E> a) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getCharacteristics() {
    return field.getCharacteristics();
  }
}
