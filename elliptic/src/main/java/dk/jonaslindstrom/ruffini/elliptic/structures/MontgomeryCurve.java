package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Multiply;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.function.UnaryOperator;

/**
 * Curve on Montgomery form <i>By<sup>2</sup> = x<sup>3</sup> + Ax<sup>2</sup> + x</i>. The field should have
 * characteristics not equal to 2, <i>A &ne; &mp; 2</i> and <i>B &ne; 0</i>.
 */
public class MontgomeryCurve<E, F extends Field<E>> implements AdditiveGroup<AffinePoint<E>> {

    protected final E A, B;
    protected final F field;

    public MontgomeryCurve(F field, E A, E B) {
        this.field = field;
        this.A = A;
        this.B = B;
        assert (!field.equals(discriminant(), field.getZero()));
    }

    public F getField() {
        return field;
    }

    public E discriminant() {
        return field.multiply(B, field.subtract(field.multiply(A, A), field.integer(4)));
    }

    public E jInvariant() {
        Multiply<E> multiply = new Multiply<>(field);
        Power<E> power = new Power<>(field);
        return field.divide(multiply.apply(1728 * 4, power.apply(this.A, 3)),
                field.add(multiply.apply(4, power.apply(this.A, 3)), multiply.apply(27, power.apply(this.B, 2))));
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

        E λ;
        if (!p.x().equals(q.x())) {
            λ = field.divide(field.subtract(q.y(), p.y()), field.subtract(q.x(), p.x()));
        } else if (field.equals(p.y(), field.negate(q.y()))) {
            return AffinePoint.pointAtInfinity();
        } else {
            λ = field.divide(
                    field.add(
                            field.multiply(field.integer(3), field.multiply(p.x(), p.x())),
                            field.multiply(field.integer(2), A, p.x()),
                            field.getIdentity()),
                    field.multiply(field.integer(2), B, p.y()));
        }

        E x = field.subtract(field.multiply(B, field.multiply(λ, λ)), field.add(p.x(), q.x(), A));
        E y = field.subtract(field.multiply(λ, field.subtract(p.x(), x)), p.y());

        return new AffinePoint<>(x, y);
    }

    @Override
    public AffinePoint<E> negate(AffinePoint<E> p) {
        return new AffinePoint<>(p.x(), field.negate(p.y()));
    }

    @Override
    public AffinePoint<E> getZero() {
        return AffinePoint.pointAtInfinity();
    }

    public String toString() {
        Polynomial<E> rhs = Polynomial.of(field, field.getZero(), field.getIdentity(), A, field.getIdentity());
        Polynomial<E> lhs = Polynomial.of(field, field.getZero(), field.getZero(), B);
        return "E: " + lhs.toString("y") + " = " + rhs.toString("x");
    }

    public E getA() {
        return A;
    }

    public E getB() {
        return B;
    }

    public Pair<ShortWeierstrassCurveAffine<E, F>, UnaryOperator<AffinePoint<E>>> getCorrespondingWeierstrassCurve() {
        E a = field.divide(field.subtract(field.integer(3), field.power(A, 2)),
                field.multiply(3, field.power(B, 2)));
        E b = field.divide(field.subtract(field.multiply(2, field.power(A, 3)), field.multiply(9, A)),
                field.multiply(27, field.power(B, 3)));

        return Pair.of(new ShortWeierstrassCurveAffine<>(field, a, b),
                p -> new AffinePoint<>(field.add(field.divide(p.x(), B), field.divide(A, field.multiply(3, B))),
                        field.divide(p.y(), B)));
    }
}
