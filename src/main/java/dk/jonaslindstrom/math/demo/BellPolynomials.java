package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.concretisations.MultivariatePolynomialRingOverRing;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigIntegers;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This demo computes the first 16 <a href="https://en.wikipedia.org/wiki/Bell_polynomials">Bell polynomials</a>.
 */
public class BellPolynomials {

  public static void main(String[] arguments) {

    System.out.println("Computing the first 16 Bell polynomials...");
    int m = 16;

    MultivariatePolynomialRingOverRing<BigInteger> ring = new MultivariatePolynomialRingOverRing<>(
        BigIntegers.getInstance(), m);

    // Initial step in recursion, B_0 = 1
    List<MultivariatePolynomial<BigInteger>> polynomials = new ArrayList<>();
    polynomials.add(ring.getIdentity());

    for (int n = 1; n < m; n++) {
      MultivariatePolynomial<BigInteger> sum = ring.getZero();
      for (int i = 0; i < n; i++) {
        BigInteger binomial = binomial(n - 1, i);
        MultivariatePolynomial<BigInteger> term = ring.multiply(polynomials.get(n - i - 1), monomial(i, m)).mapCoefficients(bi -> bi.multiply(binomial));
        sum = ring.add(sum, term);
      }
      polynomials.add(sum);
      System.out.println(sum);
    }

  }

  private static BigInteger binomial(int n, int k) {
    BigInteger value = BigInteger.ONE;
    for (int i = 0; i < k; i++) {
      value = value.multiply(BigInteger.valueOf(n-i))
          .divide(BigInteger.valueOf(i+1));
    }
    return value;
  }

  private static MultivariatePolynomial<BigInteger> monomial(int i, int n) {
    int[] degree = new int[n];
    degree[i] = 1;
    return MultivariatePolynomial.monomial(BigInteger.ONE, degree);
  }

}
