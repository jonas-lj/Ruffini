package dk.jonaslindstrom.math.util;

import java.util.stream.Stream;

public class SamePair<E> extends Pair<E, E> {

  public SamePair(E first, E second) {
    super(first, second);
  }

  public Stream<E> stream() {
    return Stream.of(first, second);
  }
}