package dk.jonaslindstrom.math.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
  public static <E> Iterable<E> iteratorToIterable(Iterator<E> i) {
    return () -> i;
  }
}
