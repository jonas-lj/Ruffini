package dk.jonaslindstrom.math.algebra.abstractions;

public abstract class AlgebraOverField<V, S> extends VectorSpace<V, S> {

  public AlgebraOverField(AdditiveGroup<V> vectors, Field<S> scalars) {
    super(vectors, scalars);
  }
  
  public abstract V product(V v, V u);

}
