package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.HilbertSpace;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GramSchmidt<Vector, Scalar> implements Function<List<Vector>, List<Vector>> {

  private VectorSpace<Vector, Scalar> vectorSpace;
  private BiFunction<Vector, Vector, Scalar> innerProduct;

  public GramSchmidt(HilbertSpace<Vector, Scalar> S) {
    this(S, S::innerProduct);
  }

  public GramSchmidt(VectorSpace<Vector, Scalar> V,
      BiFunction<Vector, Vector, Scalar> innerProduct) {
    this.vectorSpace = V;
    this.innerProduct = innerProduct;
  }

  @Override
  public List<Vector> apply(List<Vector> vectors) {
    List<Vector> U = new ArrayList<>();
    LinkedList<Vector> V = new LinkedList<>(vectors);

    Projection<Vector, Scalar> proj = new Projection<>(vectorSpace, innerProduct);
    while (!V.isEmpty()) {
      Vector v = V.poll();
      for (Vector u : U) {
        Vector p = proj.apply(v, u);
        v = vectorSpace.subtract(v, p);
      }
      U.add(v);
    }
    return U;
  }

}
