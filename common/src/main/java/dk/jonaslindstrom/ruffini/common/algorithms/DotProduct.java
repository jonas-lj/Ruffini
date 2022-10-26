package dk.jonaslindstrom.ruffini.common.algorithms;

import com.google.common.collect.Streams;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.SamePair;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.function.BiFunction;

public class DotProduct<E> implements BiFunction<Vector<E>, Vector<E>, E> {

    private final Ring<E> ring;

    public DotProduct(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public E apply(Vector<E> a, Vector<E> b) {
        assert (a.size() == b.size());
        return Streams.zip(a.stream(), b.stream(), SamePair::new).parallel().map(
                p -> ring.multiply(p.first, p.second)).reduce(ring.getZero(), ring::add);
    }

}
