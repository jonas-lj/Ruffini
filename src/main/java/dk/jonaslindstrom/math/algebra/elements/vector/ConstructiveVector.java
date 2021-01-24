package dk.jonaslindstrom.math.algebra.elements.vector;

import java.util.function.Function;
import java.util.function.IntFunction;

public class ConstructiveVector<E> extends BaseVector<E> {

  private final int d;
  private final IntFunction<E> builder;

  public ConstructiveVector(int d, IntFunction<E> builder) {
    this.d = d;
    this.builder = builder;
  }

  @Override
  public int getDimension() {
    return d;
  }

  @Override
  public E get(int i) {
    assert (i >= 0 && i < d);
    return builder.apply(i);
  }

  @Override
  public <F> Vector<F> map(Function<E, F> map) {
    return new ConstructiveVector<>(d, i -> map.apply(builder.apply(i)));
  }

}
