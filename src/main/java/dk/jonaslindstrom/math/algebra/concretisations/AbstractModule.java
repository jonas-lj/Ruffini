package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Module;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;

public abstract class AbstractModule<V, S, R extends Ring<S>> implements
    Module<V, S, R> {

  protected final AdditiveGroup<V> vectors;
  protected final R ring;

  public AbstractModule(AdditiveGroup<V> vectors, R ring) {
    this.vectors = vectors;
    this.ring = ring;
  }

  @Override
  public R getScalars() {
    return ring;
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

  @Override
  public String toString(V v) {
    return vectors.toString(v);
  }

  @Override
  public boolean equals(V v, V u) {
    return vectors.equals(v, u);
  }
}
