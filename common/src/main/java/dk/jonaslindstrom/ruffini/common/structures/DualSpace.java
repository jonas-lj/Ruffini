package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

import java.util.function.Function;

public class DualSpace<V, S, F extends Field<S>> extends AbstractVectorSpace<Function<V, S>, S, F> {

    public DualSpace(F scalars) {
        super(new RingOfFunctions<>(scalars), scalars);
    }

    @Override
    public Function<V, S> scale(S s, Function<V, S> v) {
        return x -> getScalars().multiply(s, v.apply(x));
    }

    @Override
    public String toString(Function<V, S> a) {
        return "N/A"; //TODO
    }

    @Override
    public boolean equals(Function<V, S> a, Function<V, S> b) {
        return false; //TODO
    }
}
