package dk.jonaslindstrom.math.util;

import java.util.function.BiFunction;

public class Pair<E, F> {

  public E first;
  public F second;

  public Pair(E first, F second) {
    this.first = first;
    this.second = second;
  }

  public E getFirst() {
    return first;
  }

  public F getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }

  public <G> G apply(BiFunction<E, F, G> function) {
    return function.apply(first, second);
  }

}