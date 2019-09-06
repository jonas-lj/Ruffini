package dk.jonaslindstrom.math.functional;

@FunctionalInterface
public interface IntBinaryFunction<E> {

  public E apply(Integer a, Integer b);
  
}
