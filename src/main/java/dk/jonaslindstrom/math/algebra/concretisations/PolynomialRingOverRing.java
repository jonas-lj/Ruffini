package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.Polynomial.Builder;
import dk.jonaslindstrom.math.util.Pair;
import java.util.Objects;
import java.util.stream.IntStream;

public class PolynomialRingOverRing<E> implements Ring<Polynomial<E>>, Cloneable {

  private Ring<E> ring;
  private String variable;

  public PolynomialRingOverRing(Ring<E> ring) {
    this(ring, "x");
  }

  public PolynomialRingOverRing(Ring<E> ring, String variable) {
    this.ring = ring;
    this.variable = variable;
  }

  public Ring<E> getRing() {
    return ring;
  }

  public Polynomial<E> element(@SuppressWarnings("unchecked") E ... coefficients) {
    return Polynomial.of(ring, coefficients);    
  }
  
  @Override
  public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {

    int d = a.degree() + b.degree();
    
    Polynomial.Builder<E> result = new Builder<>(ring);

      IntStream.range(0, d+1).forEach(k -> {
        a.forEach((i, ai) -> {
          b.forEach((j, bj) -> {
            if (i + j == k) {
              result.addTo(k, ring.multiply(ai, bj));
            }
          });
        });
      });

    return result.build();
  }

  @Override
  public Polynomial<E> getIdentity() {
    return Polynomial.constant(ring.getIdentity());
  }

  @Override
  public String toString(Polynomial<E> a) {
    return a.toString(variable);
  }

  @Override
  public Polynomial<E> add(Polynomial<E> a, Polynomial<E> b) {

    Polynomial.Builder<E> builder = new Builder<>(ring);
    
    a.forEach((i, ai) -> builder.set(i, ai));
    b.forEach((i, bi) -> builder.addTo(i,  bi));
    
    return builder.build();
  }

  @Override
  public Polynomial<E> negate(Polynomial<E> a) {
    return a.mapCoefficients(e -> ring.negate(e));
  }

  @Override
  public Polynomial<E> getZero() {
    return Polynomial.constant(ring.getZero());
  }

  @Override
  public boolean equals(Polynomial<E> a, Polynomial<E> b) {

    if (a.degree() != b.degree()) {
      return false;
    }
    
    for (int i = 0; i <= a.degree(); i++) {
      if (Objects.isNull(a.getCoefficient(i))) {
        if (Objects.nonNull(b.getCoefficient(i))) {
          if (ring.equals(b.getCoefficient(i), ring.getZero())) {
            return false;            
          }
        }
        continue;
      }
      
      if (Objects.isNull(b.getCoefficient(i))) {
        if (ring.equals(a.getCoefficient(i), ring.getZero())) {
          return false;
        }
        continue;
      }
      
      if (!ring.equals(a.getCoefficient(i), b.getCoefficient(i))) {
        return false;
      }
    }
    
    return true;
  }

  /**
   * Perform polynomial division, eg. finding a quotient <i>q</i> and a remainder <i>r</i> with
   * degree smaller than the divisor <i>b</i> s.t. <i>a = qb + r</i>.
   * 
   * @param a The dividend.
   * @param b The divisor. It is assumed that this is a monic polynomial.
   * @return
   */
  // @Override
  public Pair<Polynomial<E>, Polynomial<E>> divisionWithRemainder(Polynomial<E> a,
      Polynomial<E> b) {
    if (!ring.equals(b.getCoefficient(b.degree()), ring.getIdentity())) {
      throw new ArithmeticException("Divisor must be monic");
    }
    return divisionWithRemainder(a, b, ring.getIdentity());
  }

  /**
   * Perform polynomial division, eg. finding a quotient <i>q</i> and a remainder <i>r</i> with
   * degree smaller than <i>b</i> s.t. <i>a = qb + r</i>.
   * 
   * @param a The dividend.
   * @param b The divisor.
   * @param bLeadInverse An inverse of the leading coefficient of <i>b</i>.
   * @return
   */
  public Pair<Polynomial<E>, Polynomial<E>> divisionWithRemainder(Polynomial<E> a, Polynomial<E> b,
      E bLeadInverse) {
    Polynomial<E> quotient = getZero();
    Polynomial<E> remainder = a;

    int divisorDegree = b.degree();

    while (!equals(remainder, getZero())) {

      int remainderDegree = remainder.degree();
      if (remainderDegree < divisorDegree) {
        break;
      }

      E l = remainder.getCoefficient(remainderDegree);
      if (Objects.nonNull(l) && !ring.equals(l, ring.getZero())) {
        E q = ring.multiply(remainder.getCoefficient(remainderDegree), bLeadInverse);

        Polynomial<E> t = Polynomial.monomial(q, remainderDegree - divisorDegree);
        quotient = add(quotient, t);
        remainder = add(remainder, negate(multiply(t, b)));
      }

    }
    return new Pair<>(quotient, remainder);
  }

  @Override
  public String toString() {
    return ring.toString() + "[x]";
  }

}
