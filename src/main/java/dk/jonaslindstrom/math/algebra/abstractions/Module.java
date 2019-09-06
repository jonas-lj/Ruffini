package dk.jonaslindstrom.math.algebra.abstractions;

public abstract class Module<V, S> implements AdditiveGroup<V> {

  protected AdditiveGroup<V> vectors;
  protected Ring<S> scalars;

  public Module(AdditiveGroup<V> vectors, Ring<S> scalars) {
    this.vectors = vectors;
    this.scalars = scalars;
  }

  /**
   * Return the scaling <i>sv &isin; V</i> of the vector <i>v &isin; V</i> with the scalar <i>s</i>.
   * 
   * @param s
   * @param v
   * @return
   */
  public abstract V scale(S s, V v);

  /**
   * Return the field used for the scalars of this vector space.
   * 
   * @return
   */
  public Ring<S> getScalars() {
    return scalars;
  }

  @Override
  public V add(V a, V b) {
    return vectors.add(a, b);
  }

  @Override
  public V negate(V a) {
    return vectors.negate(a);
  }

  @Override
  public V getZero() {
    return vectors.getZero();
  }

  @Override
  public String toString(V a) {
    return vectors.toString(a);
  }

  @Override
  public boolean equals(V a, V b) {
    return vectors.equals(a, b);
  }

}
