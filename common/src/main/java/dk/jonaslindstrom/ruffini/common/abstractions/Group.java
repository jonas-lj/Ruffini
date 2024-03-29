package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A group is a set with an operation and an inverse operation.
 *
 * @param <E> Element type.
 */
public interface Group<E> extends Monoid<E> {

    /**
     * Return the inverse <i>a<sup>-1</sup></i>.
     */
    E invert(E a);

    /**
     * Return <i>ab<sup>-1</sup></i>.
     */
    default E divide(E a, E b) {
        return multiply(a, invert(b));
    }

}
