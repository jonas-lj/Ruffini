package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.InnerProductSpace;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class VectorSpaceOverField<E, F extends Field<E>> extends AbstractVectorSpace<Vector<E>, E, F>
    implements InnerProductSpace<Vector<E>, E, F> {

  public VectorSpaceOverField(F field, int n) {
    super(new VectorGroup<>(n, field), field);
  }

  @Override
  public Vector<E> scale(E s, Vector<E> v) {
    return v.map(e -> getScalars().multiply(s, e));
  }

  @Override
  public E innerProduct(Vector<E> v, Vector<E> u) {
    return new DotProduct<>(getScalars()).apply(v,u);
  }

  @Override
  public String toString(Vector<E> a) {
    return a.toString();
  }

  @Override
  public boolean equals(Vector<E> a, Vector<E> b) {
    return a.equals(b, getScalars()::equals);
  }
}
