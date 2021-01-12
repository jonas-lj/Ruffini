package dk.jonaslindstrom.math.algebra.algorithms;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import dk.jonaslindstrom.math.algebra.concretisations.MultivariatePolynomialRing;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial.Monomial;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class GröbnerBasis<E> implements UnaryOperator<Vector<MultivariatePolynomial<E>>> {

  private final MultivariatePolynomialRing<E> ring;
  private final Comparator<Monomial> ordering;
  private final GröbnerBasis<E>.SPolynomial<E> sPolynomial;
  private final MultivariatePolynomialDivision<E> division;

  public GröbnerBasis(MultivariatePolynomialRing<E> ring, Comparator<Monomial> ordering) {
    this.ring = ring;
    this.ordering = ordering;
    this.sPolynomial = new SPolynomial<>(ring);
    this.division = new MultivariatePolynomialDivision<>(ring);
  }

  @Override
  public Vector<MultivariatePolynomial<E>> apply(Vector<MultivariatePolynomial<E>> t) {
    for (int i = 0; i < t.getDimension(); i++) {
      for (int j = i + 1; j < t.getDimension(); j++) {

        MultivariatePolynomial<E> f = t.get(i);
        MultivariatePolynomial<E> g = t.get(j);

        MultivariatePolynomial<E> S = sPolynomial.apply(f, g);
        MultivariatePolynomial<E> r = division.apply(S, t).second;

        if (!ring.equals(r, ring.getZero())) {
          Vector<MultivariatePolynomial<E>> tNext = Vector.view(t.getDimension() + 1, k -> {
            if (k < t.getDimension()) {
              return t.get(k);
            } else {
              return r;
            }
          });
          // TODO: In next iteration, we shouldn't check all the pairs we've already checked.
          return apply(tNext);
        }
      }
    }
    return t;
  }

  private class SPolynomial<F> implements BinaryOperator<MultivariatePolynomial<F>> {

    private final MultivariatePolynomialRing<F> ring;

    private SPolynomial(MultivariatePolynomialRing<F> ring) {
      this.ring = ring;
    }

    @Override
    public MultivariatePolynomial<F> apply(MultivariatePolynomial<F> f,
        MultivariatePolynomial<F> g) {

      Monomial lmf = f.leadingMonomial(ordering);
      Monomial lmg = g.leadingMonomial(ordering);
      Monomial λ = lmf.lcm(lmg);

      F lcf = f.getCoefficient(lmf);
      MultivariatePolynomial<F> fHat =
          MultivariatePolynomial.monomial(ring.getField().invert(lcf), λ.divideBy(lmf));

      F lcg = g.getCoefficient(lmg);
      MultivariatePolynomial<F> gHat =
          MultivariatePolynomial.monomial(ring.getField().invert(lcg), λ.divideBy(lmg));

      return ring.subtract(ring.multiply(fHat, f), ring.multiply(gHat, g));
    }

  }

}
