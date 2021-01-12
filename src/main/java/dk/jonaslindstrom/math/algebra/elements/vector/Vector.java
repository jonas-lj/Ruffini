package dk.jonaslindstrom.math.algebra.elements.vector;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Vector<E> extends Iterable<E> {
  int getDimension();

  E get(int i);

  boolean equals(Vector<E> other, BiPredicate<E, E> equality);

  Stream<E> stream();

  <F> Vector<F> map(Function<E, F> map);

  <F> Vector<F> view(Function<E, F> map);

  /**
   * Return a vector padded with a given value to have a certain length. If <i>n</i> is larger than
   * the current dimension, the given padding value is used for the new entries.
   *
   * @param n
   * @return
   */
  Vector<E> pad(int n, E padding);

  @SafeVarargs
  static <F> Vector<F> of(F... e) {
    return new ConcreteVector<>(e);
  }

  static <F> Vector<F> ofList(ArrayList<F> e) {
    return new ConcreteVector<>(e);
  }

  static <F> Vector<F> of(int d, IntFunction<F> f) {
    return new ConcreteVector<>(d, f);
  }

  static <F> Vector<F> view(int d, IntFunction<F> f) {
    return new ConstructiveVector<>(d, f);
  }

  default boolean anyMatch(Predicate<E> predicate) {
    return stream().anyMatch(predicate);
  }

}
