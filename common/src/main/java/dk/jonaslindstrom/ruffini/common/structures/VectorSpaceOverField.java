package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.InnerProductSpace;
import dk.jonaslindstrom.ruffini.common.algorithms.DotProduct;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

public class VectorSpaceOverField<E, F extends Field<E>> extends
        AbstractVectorSpace<Vector<E>, E, F>
        implements InnerProductSpace<Vector<E>, E, F> {

    public VectorSpaceOverField(F field, int n) {
        super(new VectorGroup<>(n, field), field);
    }

    @Override
    public Vector<E> scale(E s, Vector<E> v) {
        return v.map(e -> getScalars().multiply(s, e));
    }

    @Override
    public E innerProduct(Vector<E> v, Vector<E> u) {
        return new DotProduct<>(getScalars()).apply(v, u);
    }

}
