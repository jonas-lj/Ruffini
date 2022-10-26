package dk.jonaslindstrom.ruffini.common.abstractions;

public interface Set<E> {

    /**
     * Returns a human readable string representation of an element in this set.
     */
    String toString(E a);

    /**
     * Returns <code>true</code> if and only if <i>a = b</i> as elements of this set.
     */
    boolean equals(E a, E b);

}
