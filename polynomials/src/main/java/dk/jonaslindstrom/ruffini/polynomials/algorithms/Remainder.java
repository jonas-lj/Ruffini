package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.List;
import java.util.function.BinaryOperator;

/** Compute the remainder of u divided by v assuming v has 1 as leading coefficient. */
class Remainder<E> implements BinaryOperator<Polynomial<E>> {

    private final PolynomialRingOverRing<E> polynomialRing;

    public Remainder(PolynomialRingOverRing<E> polynomialRing) {
        this.polynomialRing = polynomialRing;
    }

    public Polynomial<E> apply(Polynomial<E> u, Polynomial<E> v) {

        if (!v.getCoefficient(v.degree()).equals(polynomialRing.getRing().identity())) {
            throw new IllegalArgumentException("The leading coefficient of v must be 1");
        }

        int m = u.degree();
        int n = v.degree();

        if (m < n) {
            return u;
        }
        List<E> r = ArrayUtils.populate(m + 1, u::getCoefficient);
        Ring<E> ring = polynomialRing.getRing();

        for (int k = m - n; k >= 0; k--) {
            // The leading coefficient of v is 1
            E q = r.get(n + k);
            for (int j = n+k-1; j >= k; j--) {
                r.set(j, ring.subtract(r.get(j), ring.multiply(q, v.getCoefficient(j - k))));
            }
        }
        return new Polynomial<>(Vector.ofList(r.subList(0, n)), ring);
    }

}
