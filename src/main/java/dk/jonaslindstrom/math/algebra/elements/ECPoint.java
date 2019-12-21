package dk.jonaslindstrom.math.algebra.elements;

import java.util.Objects;

public class ECPoint<E> {

  public final E x;
  public final E y;
  
  public ECPoint(E x, E y) {
    this.x = x;
    this.y = y;
  }
  
  public static <F> ECPoint<F> pointAtInfinity() {
    return new ECPoint<F>(null, null);
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
