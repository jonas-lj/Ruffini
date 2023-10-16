package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * The Karatsuba algorithm for multiplying two polynomials. The algorithm was originally presented in A. Karatsuba and
 * Yu. Ofman (1962). "Multiplication of Many-Digital Numbers by Automatic Computers". Proceedings of the USSR Academy of
 * Sciences 145, and has runtime <i>O(n<sup>log 3</sup>) = O(n<sup>1.585</sup>)</i>.
 */
public class KaratsubaAlgorithm<E> implements BinaryOperator<Polynomial<E>> {

    private final PolynomialRingOverRing<E> polynomialRing;

    public KaratsubaAlgorithm(PolynomialRingOverRing<E> polynomialRing) {
        this.polynomialRing = polynomialRing;
    }

    private Polynomial<E> upper(Polynomial<E> p, int n) {
        Polynomial.Builder<E> builder = new Polynomial.Builder<>(polynomialRing.getRing());
        for (int i = n; i <= p.degree(); i++) {
            if (Objects.nonNull(p.getCoefficient(i))) {
                builder.set(i - n, p.getCoefficient(i));
            }
        }
        return builder.build();
    }

    private Polynomial<E> lower(Polynomial<E> p, int n) {
        Polynomial.Builder<E> builder = new Polynomial.Builder<>(polynomialRing.getRing());
        for (int i = 0; i < n; i++) {
            if (Objects.nonNull(p.getCoefficient(i))) {
                builder.set(i, p.getCoefficient(i));
            }
        }
        return builder.build();
    }

    @Override
    public Polynomial<E> apply(Polynomial<E> a, Polynomial<E> b) {
        int n = Math.max(a.degree(), b.degree()) + 1;
        if (n <= 2) {
            return polynomialRing.multiply(a, b);
        }

        int m = n / 2;

        Polynomial<E> a_upper = upper(a, m);
        Polynomial<E> a_lower = lower(a, m);

        Polynomial<E> b_upper = upper(b, m);
        Polynomial<E> b_lower = lower(b, m);

        Polynomial<E> d_lower = apply(a_lower, b_lower);
        Polynomial<E> d_upper = apply(a_upper, b_upper);
        Polynomial<E> d_mix = apply(polynomialRing.add(a_lower, a_upper), polynomialRing.add(b_lower, b_upper));

        return combine(d_lower, d_upper, d_mix, 2 * m, m);
    }

    private Polynomial<E> combine(Polynomial<E> d_lower, Polynomial<E> d_upper, Polynomial<E> d_mix, int n, int m) {
        Polynomial.Builder<E> builder = new Polynomial.Builder<>(polynomialRing.getRing());
        d_lower.forEach((i, c) -> {
            builder.addTo(i, c);
            builder.addTo(i + m, polynomialRing.getRing().negate(c));
        });

        d_upper.forEach((i, c) -> {
            builder.addTo(i + n, c);
            builder.addTo(i + m, polynomialRing.getRing().negate(c));
        });

        d_mix.forEach((i, c) -> {
            builder.addTo(i + m, c);
        });

        return builder.build();
    }
}
