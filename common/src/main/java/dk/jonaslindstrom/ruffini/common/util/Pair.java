package dk.jonaslindstrom.ruffini.common.util;

import com.google.common.base.Objects;

import java.util.function.BiFunction;

public class Pair<E, F> {

    public E first;
    public F second;

    public Pair(E first, F second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public E getFirst() {
        return first;
    }

    public F getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public <G> G apply(BiFunction<E, F, G> function) {
        return function.apply(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(first, pair.first) &&
                Objects.equal(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

}