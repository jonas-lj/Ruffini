package dk.jonaslindstrom.ruffini.reals.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.NormedVectorSpace;
import dk.jonaslindstrom.ruffini.common.algorithms.DotProduct;
import dk.jonaslindstrom.ruffini.common.structures.VectorSpaceOverField;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.reals.elements.ComplexNumber;
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
