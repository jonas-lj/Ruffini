package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;

public class RealNumbers implements Field<Double> {

  private static RealNumbers instance = new RealNumbers();
  
  public static RealNumbers getInstance() {
    return instance;
  }
  
  private RealNumbers() {

  }
  
  @Override
  public String toString() {
    return "ℝ";
  }

  @Override
  public Double invert(Double a) {
    return 1.0 / a;
  }

  @Override
  public Double getIdentity() {
    return 1.0;
  }

  @Override
  public Double multiply(Double a, Double b) {
    return a * b;
  }

  @Override
  public String toString(Double a) {
    return a.toString();
  }

  @Override
  public boolean equals(Double a, Double b) {
    return a.equals(b);
  }

  @Override
  public Double add(Double a, Double b) {
    return a + b;
  }

  @Override
  public Double negate(Double a) {
    return -a;
  }

  @Override
  public Double getZero() {
    return 0.0;
  }

}
