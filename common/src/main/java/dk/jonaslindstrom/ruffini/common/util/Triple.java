package dk.jonaslindstrom.ruffini.common.util;

public record Triple<E, F, G>(E first, F second, G third) {

    public E getFirst() {
        return first;
    }

    public F getSecond() {
        return second;
    }

    public G getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }


}
