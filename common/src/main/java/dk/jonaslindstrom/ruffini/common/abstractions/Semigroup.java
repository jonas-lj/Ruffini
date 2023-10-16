package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A semigroup is a set with an associative multiplication operation.
 *
 * @param <E> Element type.
 */
public interface Semigroup<E> extends Set<E> {

    /**
     * Return the result of the product <i>ab</i> in this group.
     */
    E multiply(E a, E b);

    /**
     * Return the result of the product <i>abc</i> in this group.
     */
    default E multiply(E a, E b, E c) {
        return multiply(multiply(a, b), c);
    }

    /**
     * Return the result of the product <i>abcd</i> in this group.
     */
    default E multiply(E a, E b, E c, E d) {
        return multiply(multiply(a, b), multiply(c, d));
    }

}
