package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.elements.EdwardsPoint;

import java.util.function.Function;

/**
 * Instances of this class represents a curve over a field over elements of type <code>E</code> satisfying the equation
 * <i>x<sup>2</sup> + y<sup>2</sup> = 1 + d x<sup>2</sup> y<sup>2</sup></i>.
 */
public class EdwardsCurve<E, F extends Field<E>> implements AdditiveGroup<EdwardsPoint<E>> {

    private final F field;
    private final E d;

    public EdwardsCurve(F field, E d) {
        this.field = field;
        this.d = d;
    }

    @Override
    public String toString(EdwardsPoint<E> a) {
        return a.toString();
    }

    @Override
    public boolean equals(EdwardsPoint<E> a, EdwardsPoint<E> b) {
        return field.equals(a.x(), b.x()) && field.equals(a.y(), b.y());
    }

    @SafeVarargs
    private E multiply(E... factors) {
        if (factors.length == 0) {
            return field.identity();
        }
        E p = factors[0];
        for (int i = 1; i < factors.length; i++) {
            p = field.multiply(p, factors[i]);
        }
        return p;
    }

    @Override
    public EdwardsPoint<E> add(EdwardsPoint<E> a, EdwardsPoint<E> b) {

        E n1 = field.add(field.multiply(a.x(), b.y()), field.multiply(a.y(), b.x()));
        E n2 = field.subtract(field.multiply(a.y(), b.y()), field.multiply(a.x(), b.x()));

        E p = multiply(d, a.x(), b.x(), a.y(), b.y());
        E d1 = field.add(field.identity(), p);
        E d2 = field.subtract(field.identity(), p);

        return new EdwardsPoint<>(field.divide(n1, d1), field.divide(n2, d2));
    }

    @Override
    public EdwardsPoint<E> negate(EdwardsPoint<E> a) {
        return new EdwardsPoint<>(field.negate(a.x()), a.y());
    }

    @Override
    public EdwardsPoint<E> zero() {
        return new EdwardsPoint<>(field.zero(), field.identity());
    }

    /**
     * Return an elliptic curve in Montgomery form which is birationally equivalent to this curve and a mapping from
     * points on this curve to points on the Montgomery curve.
     */
    public Pair<MontgomeryCurve<E, F>, Function<EdwardsPoint<E>, AffinePoint<E>>> getCorrespondingMontgomeryCurve() {
        E e = field.subtract(field.identity(), d);
        E A = field.subtract(field.divide(field.integer(4), e), field.integer(2));
        E B = field.invert(e);
        return Pair.of(new MontgomeryCurve<>(field, A, B),
                p -> {
                    E py = field.add(field.identity(), p.y());
                    E my = field.subtract(field.identity(), p.y());
                    E u = field.divide(py, my);
                    E v = field.divide(field.add(py, py), field.multiply(p.x(), my));
                    return new AffinePoint<>(u, v);
                });
    }

}
