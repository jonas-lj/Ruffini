package dk.jonaslindstrom.math.algebra.abstractions;

public interface AlgebraOverField<V, S, F extends Field<S>> extends VectorSpace<V, S, F> {

  V product(V v, V u);

}
