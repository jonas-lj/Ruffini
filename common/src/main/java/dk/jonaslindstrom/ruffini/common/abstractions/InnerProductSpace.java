package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A group is a set with an associative addition operation and an inverse operation.
 *
 * @param <V> Vector type.
 * @param <S> Scalar type.
 * @param <F> Scalar field type
 */
public interface InnerProductSpace<V, S, F extends Field<S>> extends VectorSpace<V, S, F> {

    /**
     * Return the inner product <i>⟨v,u⟩</i> of two vectors <i>v</i> and <i>u</i>.
     */
    S innerProduct(V v, V u);

}
