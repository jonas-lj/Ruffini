package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;

import java.util.function.BiFunction;

/**
 * Compute the bit length of an element in a ring assuming access to a bit shift operation.
 *
 * @param <E> Element type.
 */
public class BitLength<E> {

    private final BiFunction<E, Integer, E> rightShift;
    private final Ring<E> ring;

    public BitLength(Ring<E> ring, BiFunction<E, Integer, E> rightShift) {
        this.ring = ring;
        this.rightShift = rightShift;
    }

    public int bitLength(E n, int maxBitLength) {
        if (ring.isZero(n)) {
            return 0;
        }
        int m = maxBitLength >> 1;
        E nPrime = rightShift.apply(n, m);
        if (!ring.isZero(nPrime)) {
            return m + bitLength(nPrime, maxBitLength - m);
        }
        return bitLength(n, m);
    }
}
