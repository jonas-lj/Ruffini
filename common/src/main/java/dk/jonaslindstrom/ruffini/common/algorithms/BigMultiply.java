package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;

import java.math.BigInteger;
import java.util.function.BiFunction;

/**
 * Compute <i>e a</i> for and integer <i>e</i> and an element from an additive group, <i>a</i>.
 */
public class BigMultiply<E> implements BiFunction<BigInteger, E, E> {

    private final AdditiveGroup<E> group;

    public BigMultiply(AdditiveGroup<E> group) {
        this.group = group;
    }

    @Override
    public E apply(BigInteger e, E a) {
        if (e.equals(BigInteger.ZERO)) {
            return group.getZero();
        } else if (e.equals(BigInteger.ONE)) {
            return a;
        }

        BigInteger f = e;
        if (e.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
            return group.add(a, apply(e.subtract(BigInteger.ONE).shiftRight(1), group.add(a, a)));
        } else {
            return apply(e.shiftRight(1), group.add(a, a));
        }
    }

}
