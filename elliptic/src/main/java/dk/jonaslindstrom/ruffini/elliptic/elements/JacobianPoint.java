package dk.jonaslindstrom.ruffini.elliptic.elements;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

import java.util.function.Predicate;

public record JacobianPoint<E>(E X, E Y, E Z) {

    public static <F> JacobianPoint<F> pointAtInfinity(Field<F> field) {
        return new JacobianPoint<>(field.zero(), field.identity(), field.zero());
    }

    public boolean isPointAtInfinity(Predicate<E> isZero) {
        return isZero.test(Z);
    }

    public String toString() {
        return String.format("(%s : %s : %s)", X.toString(), Y.toString(), Z.toString());
    }

    public AffinePoint<E> toAffinePoint(Field<E> field) {
        if (isPointAtInfinity(field::isZero)) {
            return AffinePoint.pointAtInfinity();
        }
        E zSquared = field.multiply(Z, Z);
        return new AffinePoint<>(field.divide(X, zSquared), field.divide(Y, zSquared));
    }
}
