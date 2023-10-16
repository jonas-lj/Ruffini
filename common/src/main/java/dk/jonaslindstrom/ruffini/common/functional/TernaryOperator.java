package dk.jonaslindstrom.ruffini.common.functional;

public interface TernaryOperator<A> {
    A apply(A a, A b, A c);
}
