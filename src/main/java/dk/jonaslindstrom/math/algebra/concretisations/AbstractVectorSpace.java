package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;

public abstract class AbstractVectorSpace<V, S, F extends Field<S>> implements
    VectorSpace<V, S, F> {

  private final AdditiveGroup<V> vectors;
  private final F field;

  public AbstractVectorSpace(AdditiveGroup<V> vectors, F field) {
    this.vectors = vectors;
    this.field = field;
  }

  @Override
  public F getScalars() {
    return field;
  }

  @Override
  public V negate(V a) {
    return vectors.negate(a);
  }

  @Override
  public V add(V a, V b) {
    return vectors.add(a, b);
  }

  @Override
  public V getZero() {
    return vectors.getZero();
  }
}
