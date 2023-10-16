package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * A vector space is a module over a field.
 *
 * @param <V> Vector type.
 * @param <S> Scalar type.
 * @param <F> Field type.
 */
public interface VectorSpace<V, S, F extends Field<S>> extends Module<V, S, F> {

}
