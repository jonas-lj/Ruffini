package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Sum<E> {

  private final AdditiveGroup<E> group;

  public Sum(AdditiveGroup<E> group) {
    this.group = group;
  }

  public final E apply(IntFunction<E> f, Integer n) {
    return IntStream.range(0, n).mapToObj(f).reduce(group.getZero(), group::add);
  }

  public final E apply(List<E> inputs) {
    return inputs.stream().reduce(group.getZero(), group::add);
  }

  @SafeVarargs
  public final E apply(E... inputs) {
    return Arrays.stream(inputs).reduce(group.getZero(), group::add);
  }

}
