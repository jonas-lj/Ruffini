package dk.jonaslindstrom.math.algebra.elements;

public class EdwardsPoint<E>  {

  public final E x;
  public final E y;

  public EdwardsPoint(E x, E y) {
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return String.format("(%s, %s)", x.toString(), y.toString());
  }
}
