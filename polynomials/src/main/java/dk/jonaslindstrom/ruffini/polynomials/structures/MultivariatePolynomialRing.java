package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.MultivariatePolynomialDivision;
import dk.jonaslindstrom.ruffini.polynomials.elements.Monomial;
import dk.jonaslindstrom.ruffini.polynomials.elements.MultivariatePolynomial;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class MultivariatePolynomialRing<E>
        extends MultivariatePolynomialRingOverRing<E> implements EuclideanDomain<MultivariatePolynomial<E>> {

    private final Field<E> field;
    private final Comparator<Monomial> ordering;

    public MultivariatePolynomialRing(Field<E> field, int variables, Comparator<Monomial> ordering) {
        super(field, variables);
        this.field = field;
        this.ordering = ordering;
    }

    public MultivariatePolynomialRing(Field<E> field, int variables) {
        this(field, variables, MultivariatePolynomial.DEFAULT_ORDERING);
    }

    public Field<E> getField() {
        return field;
    }

    @Override
    public String toString() {
        return field.toString() + "(x)";
    }

    @Override
    public MultivariatePolynomial<E> identity() {
        return MultivariatePolynomial.constant(field.identity(), variables);
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
                if (field.equals(ai.second, field.zero())) {
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
                if (field.equals(bi.second, field.zero())) {
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
    public MultivariatePolynomial<E> zero() {
        return MultivariatePolynomial.constant(field.zero(), variables);
    }

    @Override
    public Pair<MultivariatePolynomial<E>, MultivariatePolynomial<E>> divide(
            MultivariatePolynomial<E> a, MultivariatePolynomial<E> b) {
        Pair<Vector<MultivariatePolynomial<E>>, MultivariatePolynomial<E>> result = new MultivariatePolynomialDivision<>(
                this, ordering).apply(a, Vector.of(b));
        return new Pair<>(result.first.get(0), result.second);
    }

    @Override
    public BigInteger norm(MultivariatePolynomial<E> a) {
        Stream<Monomial> monomials = StreamSupport.stream(a.monomials().spliterator(), true);
        return BigInteger.valueOf(monomials.mapToInt(Monomial::degree).max().orElse(0));
    }
}
