package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Monomial;
import dk.jonaslindstrom.ruffini.polynomials.elements.MultivariatePolynomial;

import java.util.Objects;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class MultivariatePolynomialRingOverRing<E>
        implements Ring<MultivariatePolynomial<E>> {

    protected final int variables;
    private final Ring<E> ring;

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
    public MultivariatePolynomial<E> identity() {
        return MultivariatePolynomial.constant(ring.identity(), variables);
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
                if (ring.equals(ai.second, ring.zero())) {
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
                if (ring.equals(bi.second, ring.zero())) {
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
    public MultivariatePolynomial<E> zero() {
        return MultivariatePolynomial.constant(ring.zero(), variables);
    }

}
