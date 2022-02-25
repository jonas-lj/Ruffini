package dk.jonaslindstrom.math.util;

import java.util.List;
import java.util.function.ToIntFunction;

public class ArgMax<A extends Comparable<A>> implements ToIntFunction<List<A>> {

  @Override
  public int applyAsInt(List<A> values) {
    if (values.isEmpty()) {
      throw new IllegalArgumentException("Unable to find max of empty list");
    }

    A max = values.get(0);
    int argMax = 0;

    for (int i = 1; i < values.size(); i++) {
      int c = max.compareTo(values.get(i));
      if (c < 0) {
        max = values.get(i);
        argMax = i;
      }
    }
    return argMax;
  }
}
