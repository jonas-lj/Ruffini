package dk.jonaslindstrom.math.algebra.elements;

import com.google.common.base.Objects;
import com.google.common.math.BigIntegerMath;
import dk.jonaslindstrom.math.util.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ConstructiveReal implements IntFunction<BigInteger> {

  private final IntFunction<BigInteger> estimator;
  private final String representation;
  private Pair<Integer, BigInteger> cache;

  public ConstructiveReal(IntFunction<BigInteger> estimator, String representation) {
    this.estimator = estimator;
    this.representation = representation;
    this.cache = new Pair<>(16, estimator.apply(16));
  }

  public static ConstructiveReal add(ConstructiveReal a, ConstructiveReal b) {
    return new ConstructiveReal(i -> BigIntegerMath.divide(a.apply(i + 2).add(b.apply(i + 2)),
        BigInteger.valueOf(4), RoundingMode.HALF_UP), a + " + " + b);
  }

  public static ConstructiveReal multiply(ConstructiveReal a, ConstructiveReal b) {
    return new ConstructiveReal(i -> {
      int s1;
      try {
        s1 = BigIntegerMath.log2(a.apply(0).add(BigInteger.TWO), RoundingMode.FLOOR) + 3;
      } catch (IllegalArgumentException e) {
        s1 = 3;
      }

      int s2;
      try {
        s2 = BigIntegerMath.log2(b.apply(0).add(BigInteger.TWO), RoundingMode.FLOOR) + 3;
      } catch (IllegalArgumentException e) {
        s2 = 3;
      }

      BigInteger n1 = a.apply(i + s2);
      BigInteger n2 = b.apply(i + s1);
      return BigIntegerMath
          .divide(n1.multiply(n2), BigInteger.ONE.shiftLeft(i + s1 + s2), RoundingMode.HALF_UP);
    }, a + "*" + b);
  }

  public static ConstructiveReal negate(ConstructiveReal a) {
    return new ConstructiveReal(i -> a.apply(i).negate(), "-" + a);
  }

  public static ConstructiveReal reciprocal(ConstructiveReal a) {
    return new ConstructiveReal(i -> {
      int s = IntStream.iterate(0, j -> j + 1)
          .filter(j -> a.apply(j).compareTo(BigInteger.valueOf(3)) >= 0)
          .findFirst().getAsInt();
      BigInteger n = a.apply(i + 2 * s + 2);
      return BigIntegerMath
          .divide(BigInteger.ONE.shiftLeft(2 * i + 2 * s + 2), n, RoundingMode.HALF_UP);
    }, "1/" + a);
  }

  public static ConstructiveReal of(int x) {
    return new ConstructiveReal(i -> BigInteger.valueOf(x).shiftLeft(i), Integer.toString(x));
  }

  public static ConstructiveReal of(double x) {
    return new ConstructiveReal(
        i -> new BigDecimal(BigInteger.ONE.shiftLeft(i)).multiply(BigDecimal.valueOf(x))
            .toBigInteger(),
        Double.toString(x));
  }

//  static Pair<Integer, Long> parse(double x) {
//    // Translate the double into sign, exponent and mantissa.
//    long bits = Double.doubleToLongBits(x);
//    // Note that the shift is sign-extended, hence the test against -1 not 1
//    boolean negative = (bits & (1L << 63)) != 0;
//    int exponent = (int) ((bits >> 52) & 0x7ffL);
//    long mantissa = bits & 0xfffffffffffffL;
//
//    // Subnormal numbers; exponent is effectively one higher,
//    // but there's no extra normalisation bit in the mantissa
//    if (exponent == 0) {
//      exponent++;
//    } else {
//      // Normal numbers; leave exponent as it is but add extra
//      // bit to the front of the mantissa
//      mantissa = mantissa | (1L << 52);
//    }
//
//    // Bias the exponent. It's actually biased by 1023, but we're
//    // treating the mantissa as m.0 rather than 0.m, so we need
//    // to subtract another 52 from it.
//    exponent -= 1075;
//
//    /* Normalize */
//    while ((mantissa & 1) == 0) {
//      /*  Mantissa is even */
//      mantissa >>= 1;
//      exponent++;
//    }
//
//    System.out.println(Math.pow(2.0, exponent) * mantissa);
//
//    return new Pair<>(exponent, negative ? -mantissa : mantissa);
//  }

  public BigDecimal estimate(int m) {
    // 10^{-n} * x = 2^{-m} y => x = 10^n / 2^m * y
    int n = 2 * (int) Math.ceil(m * Math.log10(2.0));
    BigInteger x = apply(m).multiply(BigInteger.TEN.pow(n).shiftRight(m));
    return new BigDecimal(x, n);
  }

  public static ConstructiveReal sqrt(ConstructiveReal x) {
    return new ConstructiveReal(i -> BigIntegerMath.sqrt(x.apply(2*i), RoundingMode.FLOOR),
        "sqrt(" + x + ")");
  }

//  double estimate() {
//    int e = 0;
//    BigInteger intPart = apply(0);
//    if (intPart.equals(BigInteger.ZERO)) {
//      // Negative exponent
//      do {
//        e = e - 1;
//      } while (apply(-e).equals(BigInteger.ZERO));
//    } else {
//      e = BigIntegerMath.log2(intPart, RoundingMode.DOWN);
//    }
//
//    BigInteger fraction = intPart;
//    int j = -Math.min(e, 0);
//    do {
//      j = j + 1;
//      fraction = apply(j);
//    } while (fraction.bitLength() <= 53);
//
//    // The 53'th bit is assumed to be 1
//    assert (fraction.testBit(53));
//
//    BitSet bitSet = new BitSet(64);
//    BigInteger exponent = BigInteger.valueOf(1023 + e);
//
//    for (int i = 0; i < 11; i++) {
//      bitSet.set(11 - i, exponent.testBit(i));
//    }
//
//    for (int i = 0; i < 52; i++) {
//      bitSet.set(12 + i, fraction.testBit(52 - i));
//    }
//
//    long bits = Long.reverse(bitSet.toLongArray()[0]);
//    return Double.longBitsToDouble(bits);
//  }

  @Override
  public BigInteger apply(int i) {
    if (i <= cache.first) {
      return cache.second.shiftRight(cache.first - i);
    } else {
      cache = new Pair<>(i, estimator.apply(i));
      return cache.second;
    }
  }

  public String toString() {
    return representation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    int bits = 17;
    ConstructiveReal that = (ConstructiveReal) o;

    // May differ on the last bit
    return estimator.apply(bits).subtract(that.estimator.apply(bits)).abs().compareTo(BigInteger.ONE) <= 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(estimator);
  }
}
