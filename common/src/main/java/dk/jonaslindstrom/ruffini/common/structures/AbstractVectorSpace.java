package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.VectorSpace;

public abstract class AbstractVectorSpace<V, S, F extends Field<S>> extends AbstractModule<V, S, F> implements
        VectorSpace<V, S, F> {

    public AbstractVectorSpace(AdditiveGroup<V> vectors, F field) {
        super(vectors, field);
    }

}
