package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.algorithms.IntegerRingEmbedding;

/**
 * A semiring is a set with an associative and commutative addition operation and an associative
 * multiplication operation. It is not required that each element has an additive inverse.
 *
 * @param <E> Element type.
 */
public interface SemiRing<E> extends Monoid<E>, CommutativeMonoid<E> {

    /**
     * Return the element in this ring equal to <i>1 + ⋯ + 1</i> where 1 is added to it self <i>a</i> times. If this is a
     * ring, <i>a</i> are allowed to be negative, in which case it is equal to the additive inverse of 1 is added to it self
     * <i>-a</i> times
     */
    default E integer(int a) {
        return new IntegerRingEmbedding<>(this).apply(a);
    }

    /**
     * Return the element in this ring equal to <i>ab</i>.
     */
    default E multiply(int a, E b) {
        return this.multiply(integer(a), b);
    }

    /**
     * Return the element in this ring equal to <i>abc</i>.
     */
    default E multiply(int a, E b, E c) {
        return this.multiply(integer(a), b, c);
    }

}
