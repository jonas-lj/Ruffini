package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.NormedVectorSpace;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import org.apache.commons.math3.util.FastMath;

public class RealCoordinateSpace extends VectorSpaceOverField<Double, RealNumbers> implements
    NormedVectorSpace<Vector<Double>, Double, RealNumbers> {

  public RealCoordinateSpace(int n) {
    super(RealNumbers.getInstance(), n);
  }

  @Override
  public double norm(Vector<Double> v) {
    return FastMath.sqrt(innerProduct(v, v));
  }

}
