package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.HilbertSpace;
import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;
import java.util.function.BiFunction;

/**
 * Compute the projection of a vector <i>v</i> onto another vector <i>u</i> in the given vector
 * space.
 * 
 * @author Jonas Lindstrøm (jonas.lindstrom@alexandra.dk)
 *
 * @param <Vector>
 * @param <Scalar>
 */
public class Projection<Vector, Scalar> implements BiFunction<Vector, Vector, Vector> {

  private VectorSpace<Vector, Scalar> vectorSpace;
  private BiFunction<Vector, Vector, Scalar> innerProduct;

  public Projection(HilbertSpace<Vector, Scalar> hilbertSpace) {
    this(hilbertSpace, hilbertSpace::innerProduct);
  }

  public Projection(VectorSpace<Vector, Scalar> vectorSpace,
      BiFunction<Vector, Vector, Scalar> innerProduct) {
    this.vectorSpace = vectorSpace;
    this.innerProduct = innerProduct;
  }

  @Override
  public Vector apply(Vector v, Vector u) {
    Scalar s = vectorSpace.getScalars().multiply(innerProduct.apply(u, v),
        vectorSpace.getScalars().invert(innerProduct.apply(u, u)));
    return vectorSpace.scale(s, u);
  }

}
