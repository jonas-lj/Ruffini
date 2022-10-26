package dk.jonaslindstrom.ruffini.common.functional;

@FunctionalInterface
public interface IntBinaryFunction<E> {

    E apply(Integer a, Integer b);

}
