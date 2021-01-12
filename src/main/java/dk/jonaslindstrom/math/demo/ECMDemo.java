package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.Multiply;
import dk.jonaslindstrom.math.algebra.concretisations.EllipticCurve;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassForm;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigPrimeField;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import java.math.BigInteger;
import java.util.Random;

/**
 * Implementation of the elliptic-curve factorization method (ECM). See https://en.wikipedia.org/wiki/Lenstra_elliptic-curve_factorization
 * for an explaination of the algorithm.
 */
public class ECMDemo {

  public static void main(String[] arguments) {
    Random random = new Random(1234);

    // The sixth Fermat number
    BigInteger n = BigInteger.TWO.pow(64).add(BigInteger.ONE);

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
      } catch (IllegalArgumentException e) {
        // Output the number and quit.
        System.out.println(e.getMessage());
        return;
      }
    }
  }

}
