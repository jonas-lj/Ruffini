package dk.jonaslindstrom.ruffini.common.abstractions;

public interface Module<V, S, R extends Ring<S>> extends AdditiveGroup<V> {

    /**
     * Return the scaling <i>sv &isin; V</i> of the vector <i>v &isin; V</i> with the scalar
     * <i>s</i>.
     */
    V scale(S s, V v);

    /**
     * Get the scalar ring.
     */
    R getScalars();

}
