package dk.jonaslindstrom.math.algebra.abstractions;

public interface InnerProductSpace<V, S, F extends Field<S>> extends VectorSpace<V, S, F> {

  /**
   * Return the inner product <i>⟨v,u⟩</i> of two vectors <i>v</i> and <i>u</i>.
   */
  S innerProduct(V v, V u);

}
