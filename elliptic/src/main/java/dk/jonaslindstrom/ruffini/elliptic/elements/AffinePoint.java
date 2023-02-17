package dk.jonaslindstrom.ruffini.elliptic.elements;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

import java.util.Objects;
import java.util.function.Function;

public record AffinePoint<E>(E x, E y) {

    public static <F> AffinePoint<F> pointAtInfinity() {
        return new AffinePoint<>(null, null);
    }

    public boolean isPointAtInfinity() {
        return Objects.isNull(x) && Objects.isNull(y);
    }

    public String toString() {
        if (isPointAtInfinity()) {
            return "O";
        }
        return String.format("(%s, %s)", x.toString(), y.toString());
    }

    public ProjectivePoint<E> toProjectivePoint(Field<E> field) {
        if (isPointAtInfinity()) {
            return ProjectivePoint.pointAtInfinity(field);
        }
        return new ProjectivePoint<>(x, y, field.getIdentity());
    }

    public <F> AffinePoint<F> apply(Function<E, F> function) {
        return new AffinePoint<>(function.apply(x), function.apply(y));
    }

}
