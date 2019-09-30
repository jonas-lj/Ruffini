package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.HilbertSpace;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class CoordinateSpace<E> extends HilbertSpace<Vector<E>, E> {

  public CoordinateSpace(AdditiveGroup<Vector<E>> vectors, Field<E> scalars) {
    super(vectors, scalars);
  }

  @Override
  public Vector<E> scale(E s, Vector<E> v) {
    return v.map(e -> this.scalars.multiply(s, e));
  }

  @Override
  public E innerProduct(Vector<E> v, Vector<E> u) {
    return new DotProduct<>(scalars).apply(v,u);
  }

}
