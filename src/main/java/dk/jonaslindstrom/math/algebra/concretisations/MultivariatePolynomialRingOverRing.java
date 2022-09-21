package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.polynomial.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.polynomial.Monomial;
import dk.jonaslindstrom.math.util.Pair;
import java.util.Objects;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class MultivariatePolynomialRingOverRing<E>
    implements Ring<MultivariatePolynomial<E>> {

  private final Ring<E> ring;
  protected final int variables;

  public MultivariatePolynomialRingOverRing(Ring<E> ring, int variables) {
    this.ring = ring;
    this.variables = variables;
  }

  public Ring<E> getRing() {
    return ring;
  }

  @Override
  public String toString() {
    return ring.toString() + "(x)";
  }

  @Override
  public MultivariatePolynomial<E> getIdentity() {
    return MultivariatePolynomial.constant(ring.getIdentity(), variables);
  }

  @Override
  public MultivariatePolynomial<E> multiply(MultivariatePolynomial<E> a,
      MultivariatePolynomial<E> b) {
    return MultivariatePolynomial.multiply(a, b, ring);
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
        if (ring.equals(ai.second, ring.getZero())) {
          continue;
        } else {
          return false;
        }
      }

      if (!ring.equals(ai.second, b.getCoefficient(ai.first))) {
        return false;
      }
    }

    for (Pair<Monomial, E> bi : b.coefficients()) {
      E ai = b.getCoefficient(bi.first);
      if (Objects.isNull(ai)) {
        if (ring.equals(bi.second, ring.getZero())) {
          continue;
        } else {
          return false;
        }
      }

      if (!ring.equals(bi.second, a.getCoefficient(bi.first))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public MultivariatePolynomial<E> add(MultivariatePolynomial<E> a, MultivariatePolynomial<E> b) {
    return MultivariatePolynomial.add(a, b, ring);
  }

  @Override
  public MultivariatePolynomial<E> negate(MultivariatePolynomial<E> a) {
    return a.mapCoefficients(ring::negate);
  }

  @Override
  public MultivariatePolynomial<E> getZero() {
    return MultivariatePolynomial.constant(ring.getZero(), variables);
  }

}
