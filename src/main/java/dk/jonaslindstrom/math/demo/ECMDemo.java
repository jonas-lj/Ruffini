package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.algorithms.ECM;
import java.math.BigInteger;

/**
 * Implementation of the elliptic-curve factorization method (ECM). See
 * https://en.wikipedia.org/wiki/Lenstra_elliptic-curve_factorization for an explanation of the
 * algorithm.
 */
public class ECMDemo {

  public static void main(String[] arguments) {

    BigInteger n;
    if (arguments == null || arguments.length == 0) {
      // The sixth Fermat number
      n = BigInteger.TWO.pow(64).add(BigInteger.ONE);
    } else {
      n = new BigInteger(arguments[0]);
    }

    ECM factorize = new ECM();
    BigInteger d = factorize.apply(n);
    System.out.println(d);
  }

}
