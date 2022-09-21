package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Algebra;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.word.AlgebraElement;

public class FreeAlgebra<E, R extends Ring<E>> extends FreeModule<E, R> implements Algebra<AlgebraElement<E>, E, R> {

  public FreeAlgebra(R ring, int variables) {
    super(ring, variables);
  }

  @Override
  public AlgebraElement<E> multiply(AlgebraElement<E> a, AlgebraElement<E> b) {
    return a.multiply(b, ring::multiply, ring::add);
  }
}
