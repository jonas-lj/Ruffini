package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.common.abstractions.Monoid;

import java.math.BigInteger;
import java.util.function.BiFunction;

/**
 * Compute <i>a<sup>e</sup></i> for a {@link BigInteger} <i>e</i>.
 */
public class BigPower<E> implements BiFunction<E, BigInteger, E> {

    private final Monoid<E> monoid;

    public BigPower(Monoid<E> monoid) {
        this.monoid = monoid;
    }

    @Override
    public E apply(E a, BigInteger e) {

        if (e.compareTo(BigInteger.ZERO) < 0) {
            if (!(monoid instanceof Group)) {
                throw new IllegalArgumentException("Negative exponents are only allowed for groups");
            }
            return ((Group<E>) monoid).invert(apply(a, e.negate()));
        }

        if (e.equals(BigInteger.ZERO)) {
            return monoid.getIdentity();
        } else if (e.equals(BigInteger.ONE)) {
            return a;
        }

        if (e.testBit(0)) {
            return monoid
                    .multiply(a, apply(monoid.multiply(a, a), e.subtract(BigInteger.ONE).shiftRight(1)));
        } else {
            return apply(monoid.multiply(a, a), e.shiftRight(1));
        }

    }

}
