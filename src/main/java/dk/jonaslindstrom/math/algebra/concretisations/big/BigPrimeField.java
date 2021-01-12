package dk.jonaslindstrom.math.algebra.concretisations.big;

import dk.jonaslindstrom.math.util.Triple;
import java.math.BigInteger;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.util.StringUtils;

public class BigPrimeField extends BigIntegersModuloN implements Field<BigInteger> {

  public BigPrimeField(BigInteger p) {
    super(p);
  }

  @Override
  public BigInteger invert(BigInteger a) {
    Triple<BigInteger, BigInteger, BigInteger> xgcd =
        new EuclideanAlgorithm<>(BigIntegers.getInstance()).extendedGcd(a, super.getModulus());
    if (!xgcd.first.equals(BigInteger.ONE)) {
      throw new IllegalArgumentException("The number " + a + " is not invertible.");
    }
    return xgcd.getSecond().mod(super.mod);
  }

  @Override
  public String toString() {
    return "𝔽" + StringUtils.subscript(super.mod.toString());
  }

}
