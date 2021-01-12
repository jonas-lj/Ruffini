package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Monoid;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Product<E> {

  private final Monoid<E> group;

  public Product(Monoid<E> group) {
    this.group = group;
  }

  public E apply(IntFunction<E> f, Integer n) {
    return IntStream.range(0, n).mapToObj(f).reduce(group.getIdentity(), group::multiply);
  }

  public E apply(List<E> inputs) {
    return inputs.stream().reduce(group.getIdentity(), group::multiply);
  }
}
