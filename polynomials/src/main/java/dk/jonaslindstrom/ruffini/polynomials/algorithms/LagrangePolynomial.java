package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Compute the Lagrange interpolation polynomial which is the polynomial of the lowest degree which
 * assumes a given set of points.
 */
public class LagrangePolynomial<E> implements Function<List<Pair<E, E>>, Polynomial<E>> {

    private final Field<E> field;

    public LagrangePolynomial(Field<E> field) {
        this.field = field;
    }

    @Override
    public Polynomial<E> apply(List<Pair<E, E>> points) {

        PolynomialRing<E> ring = new PolynomialRing<>(field);
        int k = points.size();

        List<Polynomial<E>> lj = new ArrayList<>();
        for (int j = 0; j < k; j++) {
            Polynomial<E> l = ring.identity();
            E xj = points.get(j).first;
            for (int m = 0; m < k; m++) {
                if (m == j) {
                    continue;
                }
                E xm = points.get(m).first;

                Polynomial<E> f = Polynomial.of(field.negate(xm), field.identity());
                f = f.scale(field.invert(field.subtract(xj, xm)), field);
                l = ring.multiply(l, f);
            }
            lj.add(l);
        }

        Polynomial<E> L = ring.zero();
        for (int j = 0; j < k; j++) {
            E yj = points.get(j).second;
            L = ring.add(L, lj.get(j).scale(yj, field));
        }

        return L;
    }

}
