package dk.jonaslindstrom.ruffini.elliptic.elements;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

import java.util.function.Predicate;

public record ProjectivePoint<E>(E X, E Y, E Z) {

    public static <F> ProjectivePoint<F> pointAtInfinity(Field<F> field) {
        return new ProjectivePoint<>(field.zero(), field.identity(), field.zero());
    }

    public boolean isPointAtInfinity(Predicate<E> isZero) {
        return isZero.test(Z);
    }

    public String toString() {
        return String.format("[%s : %s : %s]", X.toString(), Y.toString(), Z.toString());
    }

    public AffinePoint<E> toAffinePoint(Field<E> field) {
        if (isPointAtInfinity(field::isZero)) {
            return AffinePoint.pointAtInfinity();
        }
        return new AffinePoint<>(field.divide(X, Z), field.divide(Y, Z));
    }
}
