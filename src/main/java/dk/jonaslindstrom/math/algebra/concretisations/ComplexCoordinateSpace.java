package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.NormedVectorSpace;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.elements.ComplexNumber;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import org.apache.commons.math3.util.FastMath;

public class ComplexCoordinateSpace extends
    VectorSpaceOverField<ComplexNumber, ComplexNumbers> implements
    NormedVectorSpace<Vector<ComplexNumber>, ComplexNumber, ComplexNumbers> {

  public ComplexCoordinateSpace(int n) {
    super(ComplexNumbers.getInstance(), n);
  }

  @Override
  public ComplexNumber innerProduct(Vector<ComplexNumber> v, Vector<ComplexNumber> u) {
    return new DotProduct<>(getScalars()).apply(v, u.view(z -> new ComplexNumber(z.x, -z.y)));
  }

  @Override
  public double norm(Vector<ComplexNumber> v) {
    return FastMath.sqrt(innerProduct(v, v).x);
  }

}
