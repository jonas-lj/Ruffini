package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A ring is a set with an associative and commutative addition operation and an associative
 * multiplication operation.
 *
 * @param <E> Element type.
 */
public interface Ring<E> extends SemiRing<E>, AdditiveGroup<E> {

}
