package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class ModuleOverRing<E, R extends Ring<E>> extends AbstractModule<Vector<E>, E, R> {

  public ModuleOverRing(R ring, int n) {
    super(new VectorGroup<>(n, ring), ring);
  }

  @Override
  public Vector<E> scale(E s, Vector<E> v) {
    return v.map(vi -> ring.multiply(s, vi));
  }

}
