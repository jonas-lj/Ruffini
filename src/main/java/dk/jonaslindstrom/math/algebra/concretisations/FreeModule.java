package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Module;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.word.AlgebraElement;
import dk.jonaslindstrom.math.algebra.elements.word.Word;
import dk.jonaslindstrom.math.util.Pair;

public class FreeModule<E, R extends Ring<E>> implements Module<AlgebraElement<E>, E, R> {

  protected final R ring;
  protected final int variables;

  public FreeModule(R ring, int variables) {
    this.ring = ring;
    this.variables = variables;
  }

  @Override
  public AlgebraElement<E> scale(E e, AlgebraElement<E> algebraElement) {
    return algebraElement.map(ai -> ring.multiply(e, ai));
  }

  @Override
  public R getScalars() {
    return ring;
  }

  @Override
  public AlgebraElement<E> negate(AlgebraElement<E> a) {
    return a.map(ring::negate);
  }

  @Override
  public AlgebraElement<E> add(AlgebraElement<E> a, AlgebraElement<E> b) {
    return a.add(b, ring::add);
  }

  @Override
  public AlgebraElement<E> getZero() {
    return AlgebraElement.empty();
  }

  @Override
  public String toString(AlgebraElement<E> a) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    for (Pair<Word, E> term : a) {
      String coefficient = ring.toString(term.getSecond());
      if (!first) {
        if (coefficient.startsWith("-")) {
          sb.append(" - ");
          sb.append(coefficient.substring(1));
        } else {
          sb.append(" + ");
        }
      } else {
        sb.append(coefficient);
      }
      sb.append(term.first);
      first = false;
    }
    return sb.toString();
  }

  @Override
  public boolean equals(AlgebraElement<E> a, AlgebraElement<E> b) {
    return a.equals(b, ring::equals);
  }
}
