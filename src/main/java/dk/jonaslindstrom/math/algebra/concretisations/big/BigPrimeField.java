package dk.jonaslindstrom.math.algebra.concretisations.big;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.exceptions.NotInvertibleException;
import dk.jonaslindstrom.math.util.StringUtils;
import dk.jonaslindstrom.math.util.Triple;
import java.math.BigInteger;

public class BigPrimeField extends BigIntegersModuloN implements Field<BigInteger> {

  public BigPrimeField(BigInteger p) {
    super(p);
  }

  @Override
  public BigInteger invert(BigInteger a) {
    Triple<BigInteger, BigInteger, BigInteger> xgcd =
        new EuclideanAlgorithm<>(BigIntegers.getInstance()).extendedGcd(a, super.getModulus());
    if (!xgcd.first.equals(BigInteger.ONE)) {
      throw new NotInvertibleException(a);
    }
    return xgcd.getSecond().mod(super.mod);
  }

  @Override
  public String toString() {
    return "\\mathbb{F}_{" + super.mod.toString() + "}";
  }


}
