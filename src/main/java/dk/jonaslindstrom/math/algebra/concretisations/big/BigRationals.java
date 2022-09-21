package dk.jonaslindstrom.math.algebra.concretisations.big;

import dk.jonaslindstrom.math.algebra.concretisations.FieldOfFractions;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import java.math.BigInteger;

public class BigRationals extends FieldOfFractions<BigInteger> {

  private static final BigRationals instance = new BigRationals();

  private BigRationals() {
    super(BigIntegers.getInstance());
  }

  public static BigRationals getInstance() {
    return instance;
  }

  @Override
  public String toString() {
    return "\\mathbb{Q}";
  }

}
