package dk.jonaslindstrom.math.algebra.elements.vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
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

  @SafeVarargs
  public ConcreteVector(E... values) {
    this(values.length, i -> values[i]);
  }

  public ConcreteVector(int n, IntFunction<E> populator) {
    this(n, populator, false);
  }

  public ConcreteVector(int n, IntFunction<E> populator, boolean sequential) {
    IntStream indexStream = sequential ? IntStream.range(0, n) : IntStream.range(0, n).parallel();
      this.coordinates = indexStream.mapToObj(populator)
          .collect(Collectors.toCollection(ArrayList::new));
  }

  public ConcreteVector(ArrayList<E> coordinates) {
    this.coordinates = coordinates;
  }

  public ConcreteVector(List<E> coordinates) {
    this(new ArrayList<>(coordinates));
  }

  @Override
  public int size() {
    return coordinates.size();
  }

  @Override
  public E get(int i) {
    return coordinates.get(i);
  }

  @Override
  public Stream<E> stream() {
    return coordinates.stream();
  }

  @Override
  public List<E> asList() {
    return Collections.unmodifiableList(coordinates);
  }

}
