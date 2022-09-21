package dk.jonaslindstrom.math.algebra.concretisations.big;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.util.Pair;
import java.math.BigInteger;

public class BigIntegers implements EuclideanDomain<BigInteger> {

  private static final BigIntegers instance = new BigIntegers();

  private BigIntegers() {

  }

  public static BigIntegers getInstance() {
    return instance;
  }

  @Override
  public BigInteger getIdentity() {
    return BigInteger.ONE;
  }

  @Override
  public BigInteger multiply(BigInteger a, BigInteger b) {
    return a.multiply(b);
  }

  @Override
  public String toString(BigInteger a) {
    return a.toString();
  }

  @Override
  public boolean equals(BigInteger a, BigInteger b) {
    return a.equals(b);
  }

  @Override
  public BigInteger add(BigInteger a, BigInteger b) {
    return a.add(b);
  }

  @Override
  public BigInteger negate(BigInteger a) {
    return a.negate();
  }

  @Override
  public BigInteger getZero() {
    return BigInteger.ZERO;
  }

  @Override
  public Pair<BigInteger, BigInteger> divisionWithRemainder(BigInteger a, BigInteger b) {
    BigInteger[] qr = a.divideAndRemainder(b);
    return new Pair<>(qr[0], qr[1]);
  }

  @Override
  public Integer norm(BigInteger a) {
    return a.abs().intValue();
  }

  @Override
  public String toString() {
    return "\\mathbb{Z}";
  }

}
