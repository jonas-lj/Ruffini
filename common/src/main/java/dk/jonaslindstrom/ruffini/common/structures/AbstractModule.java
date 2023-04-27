package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Module;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;

public abstract class AbstractModule<V, S, R extends Ring<S>> implements
        Module<V, S, R> {

    protected final AdditiveGroup<V> vectors;
    protected final R ring;

    public AbstractModule(AdditiveGroup<V> vectors, R ring) {
        this.vectors = vectors;
        this.ring = ring;
    }

    @Override
    public R getScalars() {
        return ring;
    }

    @Override
    public V negate(V a) {
        return vectors.negate(a);
    }

    @Override
    public V add(V a, V b) {
        return vectors.add(a, b);
    }

    @Override
    public V zero() {
        return vectors.zero();
    }

    @Override
    public String toString(V v) {
        return vectors.toString(v);
    }

    @Override
    public boolean equals(V v, V u) {
        return vectors.equals(v, u);
    }
}
