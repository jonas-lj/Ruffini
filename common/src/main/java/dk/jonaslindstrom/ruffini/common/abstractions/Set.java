package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A set is a collection of elements.
 *
 * @param <E> Element type.
 */
public interface Set<E> {

    /**
     * Returns a human-readable string representation of an element in this set.
     */
    default String toString(E a) {
        return a.toString();
    }

    /**
     * Returns <code>true</code> if and only if <i>a = b</i> as elements of this set.
     */
    boolean equals(E a, E b);

}
