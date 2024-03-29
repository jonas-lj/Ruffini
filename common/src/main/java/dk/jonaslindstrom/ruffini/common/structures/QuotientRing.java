package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;

import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

public class QuotientRing<E> implements Ring<E> {

    protected final Ring<E> ring;
    private final UnaryOperator<E> reductionMap;
    private final BiPredicate<E, E> equivalenceRelation;
    protected E mod;

    public QuotientRing(final EuclideanDomain<E> ring, final E mod) {
        this(ring, e -> ring.divide(e, mod).getSecond());
        this.mod = mod;
    }

    public QuotientRing(Ring<E> ring, UnaryOperator<E> reductionMap) {
        this.ring = ring;
        this.reductionMap = reductionMap;
        this.equivalenceRelation = (a, b) -> this.ring
                .equals(this.reductionMap.apply(a), this.reductionMap.apply(b));
    }

    @Override
    public E identity() {
        return reduce(ring.identity());
    }

    @Override
    public E multiply(E a, E b) {
        return reduce(ring.multiply(a, b));
    }

    @Override
    public String toString(E a) {
        return a.toString();
    }

    @Override
    public boolean equals(E a, E b) {
        return equivalenceRelation.test(a, b);
    }

    @Override
    public E add(E a, E b) {
        return reduce(ring.add(a, b));
    }

    @Override
    public E negate(E a) {
        return reduce(ring.negate(a));
    }

    @Override
    public E zero() {
        return reduce(ring.zero());
    }

    private E reduce(E e) {
        return this.reductionMap.apply(e);
    }

    @Override
    public String toString() {
        String ideal;
        if (mod != null) {
            ideal = mod.toString() + ring.toString();
        } else {
            ideal = equivalenceRelation.toString();
        }
        return ring + "/" + ideal;
    }

}
