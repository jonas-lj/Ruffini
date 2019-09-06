package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;
import java.util.function.Function;

public class DualSpace<V, S> extends VectorSpace<Function<V, S>, S> {
  
  public DualSpace(Field<S> scalars) {
    super(new RingOfFunctions<V, S>(scalars), scalars);
  }

  @Override
  public Function<V, S> scale(S s, Function<V, S> v) {    
    return x -> scalars.multiply(s, v.apply(x));
  }

}
