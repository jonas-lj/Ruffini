package dk.jonaslindstrom.ruffini.common.abstractions;

/**
 * An inner product space is a vector space with an inner product.
 *
 * @param <V> Vector type.
 * @param <S> Scalar type.
 * @param <F> Scalar field type
 */
public interface NormedVectorSpace<V, S, F extends Field<S>> extends VectorSpace<V, S, F> {

    double norm(V v);

}
