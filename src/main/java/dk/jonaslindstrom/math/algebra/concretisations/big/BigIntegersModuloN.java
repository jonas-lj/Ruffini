package dk.jonaslindstrom.math.algebra.concretisations.big;

import dk.jonaslindstrom.math.algebra.concretisations.QuotientRing;
import dk.jonaslindstrom.math.algebra.helpers.BarrettReduction;
import java.math.BigInteger;

/**
 * This class is an implementation of <i>ℤ / nℤ</i>, eg. the integers <i>ℤ</i> modulo the principal
 * ideal generated by a given integer <i>n</i>.
 */
public class BigIntegersModuloN extends QuotientRing<BigInteger> {

  public BigIntegersModuloN(BigInteger n) {
    super(BigIntegers.getInstance(), new BarrettReduction(n));
    this.mod = n;
  }

  public BigIntegersModuloN(int n) {
    this(BigInteger.valueOf(n));
  }

  public BigInteger getModulus() {
    return mod;
  }

  @Override
  public BigInteger power(BigInteger x, int e) {
    return x.modPow(BigInteger.valueOf(e), getModulus());
  }
}
