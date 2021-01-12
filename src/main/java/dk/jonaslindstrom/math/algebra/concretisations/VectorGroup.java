package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class VectorGroup<E> implements AdditiveGroup<Vector<E>> {

  private final int n;
  private final AdditiveGroup<E> group;

  public VectorGroup(int n, AdditiveGroup<E> group) {
    this.n = n;
    this.group = group;
  }

  public Vector<E> add(Vector<E> a, Vector<E> b) {
    return Vector.of(n, i -> group.add(a.get(i), b.get(i)));
  }

  @Override
  public Vector<E> negate(Vector<E> a) {
    return Vector.of(n, i -> group.negate(a.get(i)));
  }

  @Override
  public Vector<E> getZero() {
    return Vector.of(n, i -> group.getZero());
  }

  @Override
  public String toString(Vector<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(Vector<E> a, Vector<E> b) {
    return a.equals(b, group::equals);
  }
}
