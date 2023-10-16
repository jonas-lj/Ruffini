package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Compute the sum of a list of elements over an additive group.
 *
 * @param <E> Element type.
 */
public class Sum<E> {

    private final AdditiveGroup<E> group;

    public Sum(AdditiveGroup<E> group) {
        this.group = group;
    }

    public final E apply(IntFunction<E> f, Integer n) {
        return apply(f, 0, n);
    }

    public final E apply(IntFunction<E> f, Integer n0, Integer n1) {
        return IntStream.range(n0, n1).mapToObj(f).reduce(group.zero(), group::add);
    }

    public final E apply(List<E> inputs) {
        return inputs.stream().reduce(group.zero(), group::add);
    }

    @SafeVarargs
    public final E apply(E... inputs) {
        return Arrays.stream(inputs).reduce(group.zero(), group::add);
    }

    public final E apply(Iterable<E> inputs) {
        return StreamSupport.stream(inputs.spliterator(), true).reduce(group.zero(), group::add);
    }

}
