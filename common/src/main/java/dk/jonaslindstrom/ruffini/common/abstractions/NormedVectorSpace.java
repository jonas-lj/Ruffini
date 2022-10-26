package dk.jonaslindstrom.ruffini.common.abstractions;

public interface NormedVectorSpace<V, S, F extends Field<S>> extends VectorSpace<V, S, F> {

    double norm(V v);

}
