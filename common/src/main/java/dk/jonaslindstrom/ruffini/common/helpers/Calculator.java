package dk.jonaslindstrom.ruffini.common.helpers;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Calculator<E> {

    private final AdditiveGroup<E> group;
    private Field<E> field;
    private Ring<E> ring;

    public Calculator(Field<E> field) {
        this.field = field;
        this.ring = field;
        this.group = field;
    }

    public Calculator(Ring<E> ring) {
        this.ring = ring;
        this.group = ring;
    }

    public Calculator(AdditiveGroup<E> group) {
        this.group = group;
    }

    @SafeVarargs
    public final E sum(E... terms) {
        return sum(Arrays.stream(terms).collect(Collectors.toList()));
    }

    public final E sum(Collection<E> terms) {
        E result = group.getZero();
        for (E term : terms) {
            result = group.add(result, term);
        }
        return result;
    }

    @SafeVarargs
    public final E mul(E... factors) {
        return mul(Arrays.stream(factors).collect(Collectors.toList()));
    }

    public final E mul(Collection<E> factors) {
        E result = ring.getIdentity();
        for (E factor : factors) {
            result = ring.multiply(result, factor);
        }
        return result;
    }

    @SafeVarargs
    public final E mul(int n, E... factors) {
        E result = ring.getZero();
        E a = mul(factors);
        for (int i = 0; i < n; i++) {
            result = ring.add(result, a);
        }
        return result;
    }

    public E sub(E a, E b) {
        return group.add(a, group.negate(b));
    }

    public E div(E n, E d) {
        if (field == null) {
            throw new UnsupportedOperationException();
        }
        return field.multiply(n, field.invert(d));
    }

    public E inv(E d) {
        if (field == null) {
            throw new UnsupportedOperationException();
        }
        return field.invert(d);
    }


    public E pow(E a, Integer n) {
        return new Power<E>(ring).apply(a, n);
    }

    public E sq(E a) {
        return pow(a, 2);
    }

    public E integer(Integer integer) {
        return new IntegerRingEmbedding<>(ring).apply(integer);
    }

    /**
     * Return the negation <i>-a</i> of an element <i>a</i>.
     *
     * @param a
     * @return
     */
    public E m(E a) {
        return group.negate(a);
    }
}
