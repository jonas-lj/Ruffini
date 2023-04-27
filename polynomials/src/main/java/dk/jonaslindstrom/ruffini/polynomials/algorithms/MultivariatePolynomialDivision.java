package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Monomial;
import dk.jonaslindstrom.ruffini.polynomials.elements.MultivariatePolynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.MultivariatePolynomialRing;

import java.util.Comparator;
import java.util.function.BiFunction;

public class MultivariatePolynomialDivision<E> implements
        BiFunction<MultivariatePolynomial<E>, Vector<MultivariatePolynomial<E>>, Pair<Vector<MultivariatePolynomial<E>>, MultivariatePolynomial<E>>> {

    private final MultivariatePolynomialRing<E> R;
    private final Comparator<Monomial> ordering;

    public MultivariatePolynomialDivision(Field<E> field, int variables) {
        this(field, variables, MultivariatePolynomial.DEFAULT_ORDERING);
    }

    public MultivariatePolynomialDivision(Field<E> field, int variables,
                                          Comparator<Monomial> ordering) {
        this(new MultivariatePolynomialRing<>(field, variables), ordering);
    }

    public MultivariatePolynomialDivision(MultivariatePolynomialRing<E> ring) {
        this(ring, MultivariatePolynomial.DEFAULT_ORDERING);
    }

    public MultivariatePolynomialDivision(MultivariatePolynomialRing<E> ring,
                                          Comparator<Monomial> ordering) {
        this.R = ring;
        this.ordering = ordering;
    }

    @Override
    public Pair<Vector<MultivariatePolynomial<E>>, MultivariatePolynomial<E>> apply(
            MultivariatePolynomial<E> g, Vector<MultivariatePolynomial<E>> f) {

        MultivariatePolynomial<E> ĝ = g;
        MultivariatePolynomial<E> r = R.zero();

        int n = f.size();

        Vector<MultivariatePolynomial.Builder<E>> h =
                Vector.of(n, i -> new MultivariatePolynomial.Builder<>(g.variables(), R.getField()));

        Field<E> K = R.getField();

        while (!R.equals(ĝ, R.zero())) {
            Monomial λ = ĝ.leadingMonomial(ordering);
            E c = ĝ.leadingCoefficient(ordering);

            boolean foundDivisor = false;

            for (int i = 0; i < n; i++) {
                MultivariatePolynomial<E> fᵢ = f.get(i);

                Monomial lmfᵢ = fᵢ.leadingMonomial(ordering);
                if (lmfᵢ.divides(λ)) {
                    Monomial β = λ.divideBy(lmfᵢ);
                    E q = K.divide(c, fᵢ.leadingCoefficient());

                    h.get(i).add(q, β);

                    MultivariatePolynomial<E> δ = R.multiply(MultivariatePolynomial.monomial(q, β), fᵢ);
                    ĝ = R.add(ĝ, δ);

                    foundDivisor = true;
                    break;
                }
            }

            if (!foundDivisor) {
                MultivariatePolynomial<E> t = MultivariatePolynomial.monomial(c, λ);
                ĝ = R.subtract(ĝ, t);
                r = R.add(r, t);
            }
        }

        Vector<MultivariatePolynomial<E>> result = h.map(MultivariatePolynomial.Builder::build);

        return new Pair<>(result, r);
    }
}
