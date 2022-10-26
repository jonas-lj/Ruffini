package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.common.abstractions.Monoid;

import java.util.function.BiFunction;

/**
 * Compute <i>a<sup>e</sup></i> for an integer <i>e</i> and an .
 */
public class Power<E> implements BiFunction<E, Integer, E> {

    private final Monoid<E> monoid;

    public Power(Monoid<E> monoid) {
        this.monoid = monoid;
    }

    @Override
    public E apply(E a, Integer e) {

        if (e < 0) {
            if (!(monoid instanceof Group)) {
                throw new IllegalArgumentException("Negative exponents are only allowed for groups");
            }
            return ((Group<E>) monoid).invert(apply(a, -e));
        }

        if (e == 0) {
            return monoid.getIdentity();
        } else if (e == 1) {
            return a;
        }

        if (Math.floorMod(e, 2) == 1) {
            return monoid.multiply(a, apply(monoid.multiply(a, a), (e - 1) / 2));
        } else {
            return apply(monoid.multiply(a, a), e / 2);
        }

    }

}
