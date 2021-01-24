package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.EllipticCurve;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassForm;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigPrimeField;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import dk.jonaslindstrom.math.algebra.exceptions.NotInvertibleException;
import java.math.BigInteger;
import java.util.Random;
import java.util.function.UnaryOperator;

public class ECM implements UnaryOperator<BigInteger> {

  private final Random random;

  public ECM(Random random) {
    this.random = random;
  }

  public ECM() {
    this(new Random());
  }

  @Override
  public BigInteger apply(BigInteger n) {

    // Assume for contradiction that n is prime and ℤn is a field
    Field<BigInteger> ℤn = new BigPrimeField(n);

    BigInteger a = new BigInteger(n.bitLength(), random);
    BigInteger x = new BigInteger(n.bitLength(), random);
    BigInteger y = new BigInteger(n.bitLength(), random);
    BigInteger b = ℤn.subtract(ℤn.multiply(y, y), ℤn.add(ℤn.multiply(x, x, x), ℤn.multiply(a, x)));

    EllipticCurve<ECPoint<BigInteger>> curve = new WeierstrassForm<>(ℤn, a, b);

    int B = 100000;
    ECPoint<BigInteger> P = new ECPoint<>(x, y);
    Multiply<ECPoint<BigInteger>> multiply = new Multiply<>(curve);

    for (int i = 2; i <= B; i++) {
      try {
        // Once an inversion fails it yields a number which shares a common divisor with n.
        P = multiply.apply(B, P);
      } catch (NotInvertibleException e) {
        // Output the number and quit.
        return (BigInteger) e.getElement();
      }
    }
    return null;
  }
}
