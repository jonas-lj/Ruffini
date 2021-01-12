package dk.jonaslindstrom.math.functional;

@FunctionalInterface
public interface IntBinaryFunction<E> {

  E apply(Integer a, Integer b);

}
