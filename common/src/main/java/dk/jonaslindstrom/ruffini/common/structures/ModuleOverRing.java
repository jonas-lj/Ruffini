package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

public class ModuleOverRing<E, R extends Ring<E>> extends AbstractModule<Vector<E>, E, R> {

    public ModuleOverRing(R ring, int n) {
        super(new VectorGroup<>(n, ring), ring);
    }

    @Override
    public Vector<E> scale(E s, Vector<E> v) {
        return v.map(vi -> ring.multiply(s, vi));
    }

}
