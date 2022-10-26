package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.Objects;

public class PolynomialRingOverRing<E> implements Ring<Polynomial<E>> {

    private final Ring<E> ring;
    private final String variable;

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

    @SafeVarargs
    public final Polynomial<E> element(E... coefficients) {
        return Polynomial.of(ring, coefficients);
    }

    @Override
    public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
        Polynomial.Builder<E> result = new Polynomial.Builder<>(ring);
        a.forEachInParallel(
                (i, ai) -> b.forEachInParallel((j, bj) -> result.addTo(i + j, ring.multiply(ai, bj))));
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
        Polynomial.Builder<E> builder = new Polynomial.Builder<>(ring);
        a.forEachInParallel(builder::addTo);
        b.forEachInParallel(builder::addTo);
        return builder.build();
    }

    @Override
    public Polynomial<E> negate(Polynomial<E> a) {
        return a.mapCoefficients(ring::negate);
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
     */
    public Pair<Polynomial<E>, Polynomial<E>> divisionWithRemainder(Polynomial<E> a,
                                                                    Polynomial<E> b) {
        if (!ring.isIdentity(b.getCoefficient(b.degree()))) {
            throw new ArithmeticException("Divisor must be monic");
        }
        return divisionWithRemainder(a, b, ring.getIdentity());
    }

    /**
     * Perform polynomial division, eg. finding a quotient <i>q</i> and a remainder <i>r</i> with
     * degree smaller than <i>b</i> s.t. <i>a = qb + r</i>.
     *
     * @param a            The dividend.
     * @param b            The divisor.
     * @param bLeadInverse An inverse of the leading coefficient of <i>b</i>.
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
