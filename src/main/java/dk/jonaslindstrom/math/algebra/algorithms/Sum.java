package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Sum<E> {

  private final AdditiveGroup<E> group;

  public Sum(AdditiveGroup<E> group) {
    this.group = group;
  }

  public final E apply(IntFunction<E> f, Integer n) {
    return apply(f, 0, n);
  }

  public final E apply(IntFunction<E> f, Integer n0, Integer n1) {
    return IntStream.range(n0, n1).mapToObj(f).reduce(group.getZero(), group::add);
  }

  public final E apply(List<E> inputs) {
    return inputs.stream().reduce(group.getZero(), group::add);
  }

  @SafeVarargs
  public final E apply(E... inputs) {
    return Arrays.stream(inputs).reduce(group.getZero(), group::add);
  }

  public final E apply(Iterable<E> inputs) {
    return StreamSupport.stream(inputs.spliterator(), true).reduce(group.getZero(), group::add);
  }

}
