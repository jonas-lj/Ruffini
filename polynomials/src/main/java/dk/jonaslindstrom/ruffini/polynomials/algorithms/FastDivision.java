package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.function.BiFunction;

/**
 * Algorthm 9.5 from Modern Computer Algebra. This is asymptotically faster than the usual division algorithm, and in
 * particular if the degree of the divisor is almost as large as the degree of the dividend.
 */
public class FastDivision<E> implements BiFunction<Polynomial<E>, Polynomial<E>, Pair<Polynomial<E>, Polynomial<E>>> {

    private final PolynomialRingOverRing<E> ring;

    public FastDivision(PolynomialRingOverRing<E> ring) {
        this.ring = ring;
    }

    @Override
    public Pair<Polynomial<E>, Polynomial<E>> apply(Polynomial<E> a, Polynomial<E> b) {
        if (a.degree() < b.degree()) {
            return Pair.of(ring.zero(), a);
        }

        int m = a.degree() - b.degree();

        // Compute the inverse of b(x) mod x^{m+1}
        Polynomial<E> bReverseInverse = new Inversion<>(ring.getRing()).apply(b.reverse(), m+1);

        Polynomial.Builder<E> builder = new Polynomial.Builder<>(ring.getRing());
        a.reverse().forEach((i, ai) -> {
            bReverseInverse.forEach((j, bj) -> {
                if (i+j < m+1) {
                    builder.addTo(i + j, ring.getRing().multiply(ai, bj));
                }
            });
        });
        Polynomial<E> q = builder.build().reverse();

        return Pair.of(q, ring.subtract(a, ring.multiply(b, q)));
    }
}
