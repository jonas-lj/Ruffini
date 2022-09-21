package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassCurve;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigPrimeField;
import dk.jonaslindstrom.math.algebra.elements.curves.AffinePoint;
import dk.jonaslindstrom.math.algebra.exceptions.NotInvertibleException;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.UnaryOperator;

public class ECM implements UnaryOperator<BigInteger> {

  private static final int ITERATIONS = 1000000;
  private final int B;
  private final Random random;

  public ECM(Random random, int B) {
    this.random = random;
    this.B = B;
  }

  public ECM() {
    this(new Random(), 100000);
  }

  @Override
  public BigInteger apply(BigInteger n) {

    // Assume for contradiction that n is prime and ℤn is a field
    Field<BigInteger> ℤn = new BigPrimeField(n);

    BigInteger k = BigInteger.ONE;
    for (int i = 1; i < B; i++) {
      k = k.multiply(BigInteger.valueOf(i).gcd(k));
    }

    for (int i = 0; i < ITERATIONS; i++) {
      BigInteger a = new BigInteger(n.bitLength(), random);
      BigInteger x = new BigInteger(n.bitLength(), random);
      BigInteger y = new BigInteger(n.bitLength(), random);
      BigInteger b = ℤn
          .subtract(ℤn.multiply(y, y), ℤn.add(ℤn.multiply(x, x, x), ℤn.multiply(a, x)));

      AdditiveGroup<AffinePoint<BigInteger>> curve = new WeierstrassCurve<>(ℤn, a, b);

      AffinePoint<BigInteger> P = new AffinePoint<>(x, y);
      BigMultiply<AffinePoint<BigInteger>> multiply = new BigMultiply<>(curve);

      try {
        AffinePoint<BigInteger> Q = multiply.apply(k, P);

      } catch (NotInvertibleException e) {
        // Output the number and quit.
        return (BigInteger) e.getElement();
      }

    }
    return null;
  }


}
