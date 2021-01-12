package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.util.StringUtils;
import java.util.function.Function;

public class RingOfFunctions<S, R> implements Ring<Function<S, R>> {

  private final Ring<R> ring;

  public RingOfFunctions(Ring<R> ring) {
    this.ring = ring;
  }

  @Override
  public Function<S, R> getIdentity() {
    return s -> ring.getIdentity();
  }

  @Override
  public Function<S, R> multiply(Function<S, R> a, Function<S, R> b) {
    return s -> ring.multiply(a.apply(s), b.apply(s));
  }

  @Override
  public String toString(Function<S, R> a) {
    return ring.toString() + StringUtils.superscript("S");
  }

  @Override
  public boolean equals(Function<S, R> a, Function<S, R> b) {
    return a.equals(b);
  }

  @Override
  public Function<S, R> add(Function<S, R> a, Function<S, R> b) {
    return s -> ring.add(a.apply(s), b.apply(s));
  }

  @Override
  public Function<S, R> negate(Function<S, R> a) {
    return s -> ring.negate(a.apply(s));
  }

  @Override
  public Function<S, R> getZero() {
    return s -> ring.getZero();
  }

  @Override
  public int getCharacteristics() {
    return ring.getCharacteristics();
  }
}
