package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Monoid;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Compute the product of a list of elements over a monoid.
 *
 * @param <E> Element type.
 */
public class Product<E> {

    private final Monoid<E> group;

    public Product(Monoid<E> group) {
        this.group = group;
    }

    public E apply(IntFunction<E> f, Integer n) {
        return IntStream.range(0, n).mapToObj(f).reduce(group.identity(), group::multiply);
    }

    public E apply(List<E> inputs) {
        return inputs.stream().reduce(group.identity(), group::multiply);
    }
}
