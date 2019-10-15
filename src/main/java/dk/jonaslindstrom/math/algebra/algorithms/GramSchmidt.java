package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.HilbertSpace;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GramSchmidt<V, S> implements Function<List<V>, List<V>> {

  private VectorSpace<V, S> vectorSpace;
  private BiFunction<V, V, S> innerProduct;

  public GramSchmidt(HilbertSpace<V, S> S) {
    this(S, S::innerProduct);
  }

  public GramSchmidt(VectorSpace<V, S> V,
      BiFunction<V, V, S> innerProduct) {
    this.vectorSpace = V;
    this.innerProduct = innerProduct;
  }

  @Override
  public List<V> apply(List<V> vectors) {
    List<V> U = new ArrayList<>();

    Projection<V, S> proj = new Projection<>(vectorSpace, innerProduct);
    for (V v : vectors) {
      for (V u : U) {
        V p = proj.apply(v, u);
        v = vectorSpace.subtract(v, p);
      }
      U.add(v);
    }
    return U;
  }

}
