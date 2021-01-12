package dk.jonaslindstrom.math.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultidimensionalArray<E> {

  private List<E> list;
  private final int[] dimensions;

  public MultidimensionalArray(int ... dimensions) {
    this.dimensions = dimensions;

    this.list = new ArrayList<>();
    list.addAll(Collections.nCopies(MathUtils.product(dimensions), null));
  }

  public MultidimensionalArray(E defaultValue, int ... dimensions) {
    this(c -> defaultValue, dimensions);
  }

  public MultidimensionalArray(Function<int[], E> populator, int ... dimensions) {
    this.dimensions = dimensions;
    this.list = IntStream.range(0, list.size()).mapToObj(i -> getCoordinate(i)).map(populator)
        .collect(Collectors.toList());
  }

  private int[] getCoordinate(int index) {
    int rank = dimensions.length;
    int[] result = new int[rank];

    int x = 1;
    for (int i = 1; i < rank; i++) {
      x *= dimensions[i];
    }

    result[0] = index % x;
    index -= result[0];
    for (int i = 1; i < rank; i++) {
      x /= dimensions[i];
      result[i] = index % x;
      index -= result[i];
    }

    return result;
  }

  private int getIndex(int ... coordinates) {
    assert(coordinates.length == dimensions.length);

    int j = coordinates[0];
    int x = 1;
    for (int i = 1; i < dimensions.length; i++) {
      x *= dimensions[i];
      j += coordinates[i] * x;
    }

    return j;
  }

  public E get(int ... coordinates) {
    return list.get(getIndex(coordinates));
  }

  public E set(E value, int ... coordinates) {
    return list.set(getIndex(coordinates), value);
  }
}
