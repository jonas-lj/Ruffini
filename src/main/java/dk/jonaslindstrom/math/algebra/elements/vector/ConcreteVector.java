package dk.jonaslindstrom.math.algebra.elements.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConcreteVector<E> extends BaseVector<E> {

  private final ArrayList<E> coordinates;

  public ConcreteVector(int n, Supplier<E> supplier) {
    this(n, i -> supplier.get());
  }

  public ConcreteVector(@SuppressWarnings("unchecked") E ... values) {
    this(values.length, i -> values[i]);
  }

  public ConcreteVector(int n, IntFunction<E> populator) {
    this.coordinates = IntStream.range(0, n).parallel().mapToObj(populator).collect(Collectors.toCollection(ArrayList::new));
  }

  public ConcreteVector(ArrayList<E> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public int getDimension() {
    return coordinates.size();
  }

  @Override
  public E get(int i) {
    return coordinates.get(i);
  }

  @Override
  public Iterator<E> iterator() {
    return coordinates.iterator();
  }

  @Override
  public Stream<E> stream() {
    return coordinates.stream();
  }


}
