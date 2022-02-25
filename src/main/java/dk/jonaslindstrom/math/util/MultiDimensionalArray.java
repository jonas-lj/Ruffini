package dk.jonaslindstrom.math.util;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A multi-dimensional array is a data collection where entries are indexed by a fixed length vector
 * (the length equals the dimension of the array). The elements are stored recursively using nested
 * instances {@link ArrayList}'s, so the complexity of get and set operations are linear in the
 * depth of the array.
 *
 * @param <T> The type of objects stored in the array.
 */
public abstract class MultiDimensionalArray<T> extends AbstractCollection<T> {

  /**
   * Create a new MultiDimensionalArray.
   *
   * @param <S>       The type of objects stored in the array.
   * @param width     The width of this array in the first dimension.
   * @param populator A function supplying new MultiDimensionalArrays of dimension one less than the
   *                  resulting array. The function should return arrays for input 0,1,...,width-1.
   * @return A new MultiDimensionalArray generated using the given populator.
   */
  public static <S> MultiDimensionalArray<S> build(int width,
      IntFunction<MultiDimensionalArray<S>> populator) {
    return new MultiDimensionalArrayImpl<>(width, populator);
  }

  public static <S> MultiDimensionalArray<S> sparse(List<Integer> shape) {
    return new MultiDimensionalArraySparse<>(shape);
  }

  /**
   * Create a new one-dimensional array with the given entries.
   */
  public static <S> MultiDimensionalArray<S> build(List<S> entries) {
    return new OneDimensionalArray<>(entries);
  }

  /**
   * Create a new multi-dimensional array with the given shape and with each entry being generated
   * by the populator function.
   */
  public static <S> MultiDimensionalArray<S> build(List<Integer> shape,
      Function<List<Integer>, S> populator) {
    if (shape.size() == 1) {
      return new OneDimensionalArray<>(shape.get(0), i -> populator.apply(List.of(i)));
    }
    return new MultiDimensionalArrayImpl<>(shape, populator);
  }

  private static List<Integer> prepend(int a, List<Integer> list) {
    return new PrependList<>(a, list);
  }

  private static List<Integer> append(List<Integer> list, int a) {
    return new AppendList<>(list, a);
  }

  /**
   * Given a list of dimensions <i>(l<sub>0</sub>, ..., l<sub>n-1</sub>)</i>, this method returns a
   * stream of all lists <i>k<sub>0</sub>, ..., k<sub>n-1</sub></i> with <i>0 &le; k<sub>i</sub> <
   * l<sub>i</sub></i> in lexicographical order.
   */
  private static Stream<List<Integer>> allIndices(List<Integer> dimensions) {
    if (dimensions.size() == 1) {
      return IntStream.range(0, dimensions.get(0)).mapToObj(List::of);
    }

    return IntStream.range(0, dimensions.get(0)).boxed()
        .flatMap(i -> allIndices(dimensions.subList(1, dimensions.size())).map(tail -> {
          List<Integer> out = new ArrayList<>();
          out.add(i);
          out.addAll(tail);
          return out;
        }));
  }

  /**
   * Get the element in this array with the given index vector.
   */
  public abstract T get(List<Integer> index) throws IndexOutOfBoundsException;

  /**
   * Get the element in this array with the given index vector.
   */
  public T get(Integer... index) throws IndexOutOfBoundsException {
    return get(List.of(index));
  }

  /**
   * Get the element in this array with the given index vector.
   */
  public T get(int[] index) throws IndexOutOfBoundsException {
    return get(Arrays.stream(index).boxed().collect(Collectors.toList()));
  }

  /**
   * Set a new value for the given index vector.
   */
  public abstract void set(List<Integer> index, T value) throws IndexOutOfBoundsException;

  /**
   * Set a new value for the given index vector.
   */
  public void set(int[] index, T value) throws IndexOutOfBoundsException {
    set(Arrays.stream(index).boxed().collect(Collectors.toList()), value);
  }

  /**
   * Get the shape of this array.
   */
  public abstract List<Integer> getShape();

  /**
   * Get the dimension of this array.
   */
  public abstract int getDimension();

  /**
   * Project this array into an array of dimension <i>d-1</i> using the given projection function.
   */
  public MultiDimensionalArray<T> project(Function<List<T>, T> projection) {
    assert (getDimension() > 1);
    List<Integer> dim = getShape();
    List<Integer> newShape = dim.subList(0, dim.size() - 1);

    return build(newShape, l -> projection.apply(IntStream.range(0, dim.get(dim.size() - 1))
        .mapToObj(i -> get(append(l, i))).collect(Collectors.toList())));
  }

  /**
   * Return a new {@link MultiDimensionalArray} of the same size as this, using the given function
   * to map from the elements of this array to the corresponding entry in the new array
   */
  public <S> MultiDimensionalArray<S> map(Function<T, S> function) {
    return build(this.getShape(), l -> function.apply(get(l)));
  }

  /**
   * Perform an operation on all elements
   */
  public void forEachWithIndices(BiConsumer<T, List<Integer>> consumer) {
    allIndices().forEach(i -> consumer.accept(get(i), i));
  }

  /**
   * Return a stream of all indices of this array
   */
  private Stream<List<Integer>> allIndices() {
    return allIndices(getShape());
  }

  static class OneDimensionalArray<S> extends MultiDimensionalArray<S> {

    private final List<S> entries;

    private OneDimensionalArray(List<S> entries) {
      this.entries = new ArrayList<>(entries);
    }

    private OneDimensionalArray(int size, IntFunction<S> populator) {
      this(IntStream.range(0, size).mapToObj(populator)
          .collect(Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public S get(List<Integer> index) {
      assert (index.size() == 1);
      return entries.get(index.get(0));
    }

    @Override
    public void set(List<Integer> index, S value) {
      assert (index.size() == 1);
      entries.set(index.get(0), value);
    }

    @Override
    public int getDimension() {
      return 1;
    }

    @Override
    public Iterator<S> iterator() {
      return entries.iterator();
    }

    @Override
    public int size() {
      return entries.size();
    }

    public String toString() {
      return entries.toString();
    }

    @Override
    public List<Integer> getShape() {
      return List.of(entries.size());
    }

  }

  static class MultiDimensionalArrayImpl<S> extends MultiDimensionalArray<S> {

    private final ArrayList<MultiDimensionalArray<S>> entries;
    private final int d;

    private MultiDimensionalArrayImpl(int d, IntFunction<MultiDimensionalArray<S>> entries) {
      this.d = d;
      this.entries =
          IntStream.range(0, d).mapToObj(entries).collect(Collectors.toCollection(ArrayList::new));
    }

    private MultiDimensionalArrayImpl(List<Integer> shape,
        Function<List<Integer>, S> populator) {
      assert (shape.size() > 1);
      this.d = shape.size();
      this.entries = IntStream.range(0, shape.get(0)).mapToObj(
          i -> build(shape.subList(1, shape.size()), l -> populator.apply(prepend(i, l))))
          .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public S get(List<Integer> index) {
      assert (index.size() == d);
      return entries.get(index.get(0)).get(index.subList(1, index.size()));
    }

    @Override
    public void set(List<Integer> index, S value) {
      assert (index.size() == d);
      entries.get(index.get(0)).set(index.subList(1, index.size()), value);
    }

    @Override
    public int getDimension() {
      return d;
    }

    @Override
    public Iterator<S> iterator() {
      return entries.stream().flatMap(Collection::stream).iterator();
    }

    @Override
    public int size() {
      return entries.parallelStream().mapToInt(MultiDimensionalArray::size).sum();
    }

    public String toString() {
      return entries.toString();
    }

    @Override
    public List<Integer> getShape() {
      return prepend(entries.size(), entries.get(0).getShape());
    }
  }

  /**
   * An immutable list consisting of an element prepended to an existing list
   */
  private static class PrependList<E> extends AbstractList<E> {

    private final List<E> list;
    private final E e;

    private PrependList(E e, List<E> list) {
      this.e = e;
      this.list = list;
    }

    @Override
    public E get(int index) {
      if (index == 0) {
        return e;
      } else {
        return list.get(index - 1);
      }
    }

    @Override
    public int size() {
      return list.size() + 1;
    }

  }

  /**
   * An immutable list consisting of an element appended to an existing list
   */
  private static class AppendList<E> extends AbstractList<E> {

    private final List<E> list;
    private final E e;

    private AppendList(List<E> list, E e) {
      this.list = list;
      this.e = e;
    }

    @Override
    public E get(int index) {
      if (index < list.size()) {
        return list.get(index);
      } else if (index == list.size()) {
        return e;
      } else {
        throw new IndexOutOfBoundsException();
      }
    }

    @Override
    public int size() {
      return list.size() + 1;
    }

  }

  private static class MultiDimensionalArraySparse<S> extends MultiDimensionalArray<S> {

    private final static Comparator<List<Integer>> COMPARATOR = new IndexComparator();
    private final SortedMap<List<Integer>, S> entries;
    private final List<Integer> shape;

    public MultiDimensionalArraySparse(List<Integer> shape) {
      this.entries = new TreeMap<>(COMPARATOR);
      this.shape = shape;
    }

    @Override
    public S get(List<Integer> index) throws IndexOutOfBoundsException {
      return entries.get(index);
    }

    @Override
    public void set(List<Integer> index, S value) throws IndexOutOfBoundsException {
      entries.put(index, value);
    }

    @Override
    public List<Integer> getShape() {
      return shape;
    }

    @Override
    public int getDimension() {
      return shape.size();
    }

    @Override
    public Iterator<S> iterator() {
      return MultiDimensionalArray.allIndices(shape).map(this::get).iterator();
    }

    @Override
    public int size() {
      return shape.stream().reduce(1, (a,b) -> a*b);
    }

    private static class IndexComparator implements Comparator<List<Integer>> {

      @Override
      public int compare(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
          throw new IllegalArgumentException("Lists must have same size");
        }
        for (int i = 0; i < a.size(); i++) {
          int c = Integer.compare(a.get(i), b.get(i));
          if (c != 0) {
            return c;
          }
        }
        return 0;
      }
    }
  }
}
