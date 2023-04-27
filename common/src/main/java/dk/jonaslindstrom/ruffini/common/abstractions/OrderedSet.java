package dk.jonaslindstrom.ruffini.common.abstractions;

import java.util.Comparator;

public interface OrderedSet<E> extends Set<E> {

    Comparator<E> getOrdering();

    default boolean lessThan(E a, E b) {
        return getOrdering().compare(a, b) < 0;
    }

    default boolean lessThanOrEqual(E a, E b) {
        return getOrdering().compare(a, b) <= 0;
    }

    default boolean greaterThan(E a, E b) {
        return getOrdering().compare(a, b) > 0;
    }

    default boolean greaterThanOrEqual(E a, E b) {
        return getOrdering().compare(a, b) >= 0;
    }


}
