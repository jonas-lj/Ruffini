package dk.jonaslindstrom.math.util;

import java.util.Iterator;

public class StreamUtils {

  public static <E> Iterable<E> iteratorToIterable(Iterator<E> i) {
    return () -> i;
  }
}
