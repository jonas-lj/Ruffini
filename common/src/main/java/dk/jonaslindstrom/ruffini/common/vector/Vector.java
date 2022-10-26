package dk.jonaslindstrom.ruffini.common.vector;

import com.google.common.collect.Streams;

import java.util.Collection;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Vector<E> extends Collection<E> {

    @SafeVarargs
    static <F> Vector<F> of(F... e) {
        return new ConcreteVector<>(e);
    }

    static <F> Vector<F> ofList(List<F> e) {
        return new ConcreteVector<>(e);
    }

    static <F> Vector<F> of(int d, IntFunction<F> f) {
        return of(d, f, false);
    }

    static <F> Vector<F> of(int d, IntFunction<F> f, boolean sequential) {
        return new ConcreteVector<>(d, f, sequential);
    }

    static <F> Vector<F> view(int d, IntFunction<F> f) {
        return new ConstructiveVector<>(d, f);
    }

    static <E, F> Vector<F> op(Vector<E> a, Vector<E> b, BiFunction<E, E, F> op) {
        return Vector.ofList(Streams.zip(a.stream(), b.stream(), op).collect(
                Collectors.toList()));
    }

    static Vector<Double> fromArray(double[] array) {
        return Vector.view(array.length, i -> array[i]);
    }

    int size();

    E get(int i);

    boolean equals(Vector<E> other, BiPredicate<E, E> equality);

    Stream<E> stream();

    <F> Vector<F> map(Function<E, F> map);

    <F> Vector<F> view(Function<E, F> map);

    /**
     * Return a vector padded with a given value to have a certain length. If <i>n</i> is larger than
     * the current dimension, the given padding value is used for the new entries.
     */
    Vector<E> pad(int n, E padding);

    default boolean anyMatch(Predicate<E> predicate) {
        return stream().parallel().anyMatch(predicate);
    }

    List<E> asList();

}
