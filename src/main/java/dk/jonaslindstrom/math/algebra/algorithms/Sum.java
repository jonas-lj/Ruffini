package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Sum<E> implements BiFunction<IntFunction<E>, Integer, E> {

  private AdditiveGroup<E> group;

  public Sum(AdditiveGroup<E> group) {
    this.group = group;
  }

  @Override
  public E apply(IntFunction<E> f, Integer n) {
    return IntStream.range(0, n).mapToObj(f).reduce(group.getZero(), group::add);
  }

}
