package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;

import java.util.function.Function;

public class RingOfFunctions<S, R> implements Ring<Function<S, R>> {

    private final Ring<R> ring;

    public RingOfFunctions(Ring<R> ring) {
        this.ring = ring;
    }

    @Override
    public Function<S, R> identity() {
        return s -> ring.identity();
    }

    @Override
    public Function<S, R> multiply(Function<S, R> a, Function<S, R> b) {
        return s -> ring.multiply(a.apply(s), b.apply(s));
    }

    @Override
    public String toString(Function<S, R> a) {
        return ring.toString() + "^{S}";
    }

    @Override
    public boolean equals(Function<S, R> a, Function<S, R> b) {
        return a.equals(b);
    }

    @Override
    public Function<S, R> add(Function<S, R> a, Function<S, R> b) {
        return s -> ring.add(a.apply(s), b.apply(s));
    }

    @Override
    public Function<S, R> negate(Function<S, R> a) {
        return s -> ring.negate(a.apply(s));
    }

    @Override
    public Function<S, R> zero() {
        return s -> ring.zero();
    }

}
