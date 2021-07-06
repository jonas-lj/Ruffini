package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.ConstructiveReal;
import java.math.BigInteger;

/**
 * The real numbers represented as constructive reals, e.g. arbitrary good binary approximations.
 */
public class ConstructiveReals implements Field<ConstructiveReal> {

  @Override
  public ConstructiveReal negate(ConstructiveReal a) {
    return ConstructiveReal.negate(a);
  }

  @Override
  public ConstructiveReal add(ConstructiveReal a, ConstructiveReal b) {
    return ConstructiveReal.add(a, b);
  }

  @Override
  public ConstructiveReal getZero() {
    return new ConstructiveReal(i -> BigInteger.ZERO, "0");
  }

  @Override
  public ConstructiveReal invert(ConstructiveReal a) {
    return ConstructiveReal.reciprocal(a);
  }

  @Override
  public ConstructiveReal getIdentity() {
    return new ConstructiveReal(i -> BigInteger.ONE.shiftLeft(i), "1");
  }

  @Override
  public ConstructiveReal multiply(ConstructiveReal a, ConstructiveReal b) {
    return ConstructiveReal.multiply(a, b);
  }

  @Override
  public String toString(ConstructiveReal a) {
    return a.toString();
  }

  @Override
  public boolean equals(ConstructiveReal a, ConstructiveReal b) {
    return a.equals(b);
  }
}
