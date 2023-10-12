package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Compute the Lagrange interpolation polynomial which is the polynomial of the lowest degree which
 * assumes a given set of points.
 */
public class LagrangePolynomial<E> implements BiFunction<List<E>, List<E>, Polynomial<E>> {

    private final Field<E> field;

    public LagrangePolynomial(Field<E> field) {
        this.field = field;
    }

    @Override
    public Polynomial<E> apply(List<E> x, List<E> y) {

        PolynomialRing<E> ring = new PolynomialRing<>(field);
        int k = x.size();

        List<Polynomial<E>> lj = new ArrayList<>();
        for (int j = 0; j < k; j++) {
            Polynomial<E> l = ring.identity();
            for (int m = 0; m < k; m++) {
                if (m == j) {
                    continue;
                }

                Polynomial<E> f = Polynomial.of(field.negate(x.get(m)), field.identity());
                f = f.scale(field.invert(field.subtract(x.get(j), x.get(m))), field);
                l = ring.multiply(l, f);
            }
            lj.add(l);
        }

        Polynomial<E> L = ring.zero();
        for (int j = 0; j < k; j++) {
            L = ring.add(L, lj.get(j).scale(y.get(j), field));
        }

        return L;
    }

}
