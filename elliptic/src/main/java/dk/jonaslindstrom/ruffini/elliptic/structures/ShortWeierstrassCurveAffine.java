package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.ruffini.common.algorithms.Multiply;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;


public class ShortWeierstrassCurveAffine<E, F extends Field<E>> implements AdditiveGroup<AffinePoint<E>> {

    private final E a, b;
    private final E three;
    private final F field;

    /**
     * Curve on Weierstrass form. Field should have characteristics not equal to 2 or 3.
     */
    public ShortWeierstrassCurveAffine(F field, E a, E b) {
        this.field = field;
        this.a = a;
        this.b = b;
        assert (!field.equals(discriminant(), field.zero()));
        this.three = new IntegerRingEmbedding<>(field).apply(3);
    }

    public F getField() {
        return field;
    }

    public E discriminant() {
        Multiply<E> multiply = new Multiply<>(field);
        Power<E> power = new Power<>(field);

        return field.negate(multiply.apply(16,
                field.add(
                        multiply.apply(4, power.apply(a, 3)),
                        multiply.apply(27, field.multiply(b, b)))));
    }

    @Override
    public String toString(AffinePoint<E> a) {
        return a.toString();
    }

    @Override
    public boolean equals(AffinePoint<E> a, AffinePoint<E> b) {
        if (a.isPointAtInfinity()) {
            return b.isPointAtInfinity();
        } else if (b.isPointAtInfinity()) {
            return a.isPointAtInfinity();
        }
        return field.equals(a.x(), b.x()) && field.equals(a.y(), b.y());
    }

    @Override
    public AffinePoint<E> add(AffinePoint<E> p, AffinePoint<E> q) {
        if (p.isPointAtInfinity()) {
            return q;
        }

        if (q.isPointAtInfinity()) {
            return p;
        }

        E s;
        if (field.equals(p.x(), q.x())) {
            if (field.equals(p.y(), field.negate(q.y()))) {
                return AffinePoint.pointAtInfinity();
            }
            s = field.divide(field.add(field.multiply(three, p.x(), p.x()), a),
                    field.add(p.y(), p.y()));
        } else {
            s = field.divide(field.subtract(q.y(), p.y()), field.subtract(q.x(), p.x()));
        }

        E x = field.subtract(field.multiply(s, s), field.add(p.x(), q.x()));
        E y = field.subtract(field.multiply(s, field.subtract(p.x(), x)), p.y());
        return new AffinePoint<>(x, y);
    }

    @Override
    public AffinePoint<E> negate(AffinePoint<E> p) {
        return new AffinePoint<>(p.x(), field.negate(p.y()));
    }

    @Override
    public AffinePoint<E> zero() {
        return AffinePoint.pointAtInfinity();
    }

    public String toString() {
        return String.format("E: y^2 = x^3 + %s x + %s", a, b);
    }

    public E getA() {
        return a;
    }

    public E getB() {
        return b;
    }
}
