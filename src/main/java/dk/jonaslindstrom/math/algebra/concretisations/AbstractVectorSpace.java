package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;

public abstract class AbstractVectorSpace<V, S, F extends Field<S>> extends AbstractModule<V, S, F> implements
    VectorSpace<V, S, F> {

  public AbstractVectorSpace(AdditiveGroup<V> vectors, F field) {
    super(vectors, field);
  }

}
