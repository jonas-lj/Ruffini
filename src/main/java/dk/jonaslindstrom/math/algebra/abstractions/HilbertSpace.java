package dk.jonaslindstrom.math.algebra.abstractions;

public abstract class HilbertSpace<V, S> extends VectorSpace<V, S> {

  public HilbertSpace(AdditiveGroup<V> vectors, Field<S> scalars) {
    super(vectors, scalars);
  }
  
  public abstract S innerProduct(V v, V u);

}
