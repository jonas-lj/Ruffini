package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Group;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.List;

public class ProductGroup<E, F> implements Group<Pair<E, F>> {

  private final Group<E> g;
  private final Group<F> h;

  public ProductGroup(Group<E> g, Group<F> h) {
    this.g = g;
    this.h = h;
  }

  public static ProductGroup<?, ?> of(List<Group<?>> groups) {
    return new ProductGroup<>(groups.get(0), of(groups.subList(1, groups.size())));
  }

  @Override
  public Pair<E, F> getIdentity() {
    return new Pair<>(g.getIdentity(), h.getIdentity());
  }

  @Override
  public Pair<E, F> multiply(Pair<E, F> a, Pair<E, F> b) {
    return new Pair<>(g.multiply(a.first, b.first), h.multiply(a.second, b.second));
  }

  @Override
  public String toString(Pair<E, F> a) {
    if (g == h) {
      return g.toString() + StringUtils.superscript("2");
    } else {
      return g.toString() + "×" + h.toString();
    }
  }

  @Override
  public boolean equals(Pair<E, F> a, Pair<E, F> b) {
    return g.equals(a.first, b.first) && h.equals(a.second, b.second);
  }

  @Override
  public Pair<E, F> invert(Pair<E, F> a) {
    return new Pair<>(g.invert(a.first), h.invert(a.second));
  }

}
