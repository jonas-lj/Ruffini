package dk.jonaslindstrom.math.algebra.concretisations.big;

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
    BigInteger m =
        new EuclideanAlgorithm<>(BigIntegers.getInstance()).extendedGcd(a, super.mod).getSecond();
    BigInteger modM = m.mod(super.mod);
    return modM;
  }

  @Override
  public String toString() {
    return "𝔽" + StringUtils.subscript(super.mod.toString());
  }

}
