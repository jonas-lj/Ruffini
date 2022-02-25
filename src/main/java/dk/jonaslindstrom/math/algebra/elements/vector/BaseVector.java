package dk.jonaslindstrom.math.algebra.elements.vector;

import dk.jonaslindstrom.math.util.StringUtils;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class BaseVector<E> implements Vector<E> {

  @Override
  public String toString() {
    return asColumn().toString();
  }

  @Override
  public boolean equals(Vector<E> other, BiPredicate<E, E> equality) {
    for (int i = 0; i < getDimension(); i++) {
      if (!equality.test(this.get(i), other.get(i))) {
        return false;
      }
    }
    return true;
  }


  @Override
  public Iterator<E> iterator() {
    return stream().iterator();
  }

  @Override
  public Stream<E> stream() {
    return IntStream.range(0, this.getDimension()).mapToObj(this::get);
  }

  @Override
  public <F> Vector<F> map(Function<E, F> map) {
    return new ConcreteVector<>(this.getDimension(), i -> map.apply(get(i)));
  }

  @Override
  public <F> Vector<F> view(Function<E, F> map) {
    return new ConstructiveVector<>(this.getDimension(), i -> map.apply(get(i)));
  }

  @Override
  public Vector<E> pad(int n, E padding) {
    return new ConstructiveVector<>(n, i -> {
      if (i < getDimension()) {
        return get(i);
      } else {
        return padding;
      }
    });
  }

}
