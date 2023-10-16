package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.algorithms.IntegerRingEmbedding;

/**
 * A field is a commutative ring where every non-zero element has a multiplicative inverse.
 *
 * @param <E> Element type.
 */
public interface Field<E> extends Group<E>, Ring<E> {

    /**
     * Return <i>ab<sup>-1</sup></i>.
     */
    default E divideInt(E a, int b) {
        return multiply(a, invert(new IntegerRingEmbedding<>(this).apply(b)));
    }

}
