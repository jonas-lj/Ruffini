package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.algorithms.Power;

/**
 * A monoid is a set with an associative operation.
 *
 * @param <E> Element type.
 */
public interface Monoid<E> extends Semigroup<E> {

    /**
     * Return the identity element of this monoid.
     */
    E identity();

    default boolean isIdentity(E a) {
        return this.equals(a, this.identity());
    }

    /**
     * Return <i>x<sup>e</sup></i>
     */
    default E power(E x, int e) {
        return new Power<>(this).apply(x, e);
    }
}
