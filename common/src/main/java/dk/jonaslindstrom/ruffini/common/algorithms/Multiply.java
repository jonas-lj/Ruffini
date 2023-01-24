package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;

import java.math.BigInteger;

/**
 * Compute <i>e a</i> for and integer <i>e</i> and an element from an additive group, <i>a</i>.
 */
public class Multiply<E> {

    private final AdditiveGroup<E> group;

    public Multiply(AdditiveGroup<E> group) {
        this.group = group;
    }

    public E apply(Integer e, E a) {
        if (e == 0) {
            return group.getZero();
        } else if (e == 1) {
            return a;
        }

        int f = e;
        if (f % 2 == 1) {
            return group.add(a, apply((e - 1) / 2, group.add(a, a)));
        } else {
            return apply(e / 2, group.add(a, a));
        }
    }

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
