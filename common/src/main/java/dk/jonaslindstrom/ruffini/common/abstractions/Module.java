package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A module over a ring <i>R</i> is an additive group <i>V</i> together with a scalar multiplication.
 *
 * @param <V> Vector type.
 * @param <S> Scalar type.
 * @param <R> Scalar ring type
 */
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
