package dk.jonaslindstrom.math.algebra.elements;

public class ComplexNumber {

  public final double x, y;

  public ComplexNumber(double x, double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return String.format("%f+%fi", x, y);
  }
}
