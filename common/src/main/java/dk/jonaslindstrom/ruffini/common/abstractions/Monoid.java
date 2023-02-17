package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.algorithms.Product;

import java.util.Arrays;

public interface Monoid<E> extends Semigroup<E> {

    /**
     * Return the identity element of this monoid.
     */
    E getIdentity();

    default boolean isIdentity(E a) {
        return this.equals(a, this.getIdentity());
    }

    /**
     * Return <i>x<sup>e</sup></i>
     */
    default E power(E x, int e) {
        return new Power<>(this).apply(x, e);
    }
}
