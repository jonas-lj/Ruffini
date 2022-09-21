package dk.jonaslindstrom.math.algebra.elements.curves;

import java.util.Objects;

public class AffinePoint<E> {

  public final E x;
  public final E y;

  public AffinePoint(E x, E y) {
    this.x = x;
    this.y = y;
  }

  public static <F> AffinePoint<F> pointAtInfinity() {
    return new AffinePoint<>(null, null);
  }

  public boolean isPointAtInfinity() {
    return Objects.isNull(x);
  }

  public String toString() {
    if (isPointAtInfinity()) {
      return "O";
    }
    return String.format("(%s, %s)", x.toString(), y.toString());
  }

}
