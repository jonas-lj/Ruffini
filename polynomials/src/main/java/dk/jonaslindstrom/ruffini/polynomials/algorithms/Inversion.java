package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.MathUtils;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.function.BiFunction;

/**
 * Assuming f(0) = 1, this computes g such that fg = 1 (mod x^l).
 * This is based on Algorithm 9.3 in Modern Computer Algebra.
 */
public class Inversion<E> implements BiFunction<Polynomial<E>, Integer, Polynomial<E>> {

    private final Ring<E> ring;

    public Inversion(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public Polynomial<E> apply(Polynomial<E> f, Integer l) {
        Polynomial<E> g = Polynomial.constant(ring.identity());
        int r = MathUtils.ceilLog2(l);
        for (int i = 1; i <= r; i++) {
            g = iterate(f, g, i);
        }
        return g;
    }

    private Polynomial<E> iterate(Polynomial<E> f, Polynomial<E> g, int i) {
        Polynomial.Builder<E> builder = new Polynomial.Builder<>(this.ring);

        g.forEach((j, gj) -> {
            builder.addTo(j, ring.doubling(gj));
        });

        int limit = 1 << i;
        g.forEach((k, gk) -> {
            g.forEach((l, gl) -> {
                if (k <= l && k + l < limit) {
                    E gSquared = ring.multiply(gk, gl);
                    f.forEach((j, fj) -> {
                        int power = j + k + l;
                        if (power < limit) {
                            E term = ring.multiply(gSquared, fj);
                            if (!k.equals(l)) {
                                term = ring.doubling(term);
                            }
                            builder.addTo(power, ring.negate(term));
                        }
                    });
                }
            });
        });

        return builder.build();
    }
}
