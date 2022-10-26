package dk.jonaslindstrom.ruffini.reals.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.NormedVectorSpace;
import dk.jonaslindstrom.ruffini.common.structures.VectorSpaceOverField;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
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
