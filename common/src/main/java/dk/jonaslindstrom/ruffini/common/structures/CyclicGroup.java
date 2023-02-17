package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;

import java.math.BigInteger;

public class CyclicGroup<E> implements Group<E> {

    private final Group<E> group;
    private final E generator;

    /** Create a cyclic subgroup of the given group with generator <i>g</i>. */
    public CyclicGroup(Group<E> group, E g) {
        this.group = group;
        this.generator = g;
    }

    @Override
    public E invert(E a) {
        return group.invert(a);
    }

    @Override
    public E getIdentity() {
        return group.getIdentity();
    }

    @Override
    public E multiply(E a, E b) {
        return group.multiply(a, b);
    }

    @Override
    public String toString(E a) {
        return group.toString(a);
    }

    @Override
    public boolean equals(E a, E b) {
        return group.equals(a, b);
    }

    /** Return the generator <i>g</i> for this cyclic group */
    public E getGenerator() {
        return generator;
    }

    /** Compute <i>g<sup>e</sup></i> in this group. */
    public E generatorExponent(BigInteger e) {
        return new Power<>(this).apply(generator, e);
    }
}