package demo;

import com.google.common.math.BigIntegerMath;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.BigPower;
import dk.jonaslindstrom.ruffini.common.structures.QuotientRing;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.integers.algorithms.EulersTotientFunction;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of the AKS deterministic primality test.
 */
public class AKS {

    public static void main(String[] arguments) {
        test(BigInteger.valueOf(31));
        test(BigInteger.valueOf(91));
        test(new BigInteger("991"));
        test(new BigInteger("14197"));
        test(new BigInteger("4294967297"));
    }
    private static void test(BigInteger n) {
        System.out.println(n + " is " + (aks(n) ? "" : "not ") + "a prime.");
    }

    private static boolean aks(BigInteger n) {
        int log2 = BigIntegerMath.log2(n, RoundingMode.UP);
        int t0 = log2 * log2;

        // Find smallest r such that n has large order mod r
        int r = Stream.iterate(BigInteger.TWO, s -> s.add(BigInteger.ONE))
                .filter(s -> orderExceeding(n, s, t0)).findFirst().get().intValue();

        // Trial division for 2, ... , r
        if (IntStream.rangeClosed(2, r).parallel().anyMatch(a -> n.mod(BigInteger.valueOf(a)).equals(BigInteger.ZERO))) {
            return false;
        }

        if (n.compareTo(BigInteger.valueOf(r)) <= 0) {
            return false;
        }

        // Construct ℤₙ[x] / <x^r - 1>
        BigPrimeField ℤₙ = new BigPrimeField(n);
        PolynomialRing<BigInteger> ℤₙx = new PolynomialRing<>(ℤₙ);
        Ring<Polynomial<BigInteger>> ℤₙxmodP = new QuotientRing<>(ℤₙx,
                ℤₙx.subtract(Polynomial.monomial(BigInteger.ONE, r), Polynomial.constant(BigInteger.ONE)));

        // A witness built from a should be not equal to xⁿ + a, so we compute xⁿ and reuse it
        BigPower<Polynomial<BigInteger>> power = new BigPower<>(ℤₙxmodP);
        Polynomial<BigInteger> xⁿ = power.apply(Polynomial.monomial(BigInteger.ONE, 1), n);

        // Compute Euler's totient function of r
        BigInteger φ = new EulersTotientFunction().apply(BigInteger.valueOf(r));

        // Check all potential witnesses. If there are no witness, return "true".
        return IntStream.range(1, (int) (Math.sqrt(φ.doubleValue()) * log2)).parallel()
                .mapToObj(a ->
                        new Pair<>(power.apply(
                                new Polynomial.Builder<>(ℤₙ).set(0, BigInteger.valueOf(a)).set(1, BigInteger.ONE)
                                        .build(), n),
                                ℤₙxmodP.add(xⁿ, Polynomial.constant(BigInteger.valueOf(a)))))
                .allMatch(pair -> ℤₙxmodP.equals(pair.first, pair.second));
    }

    /**
     * Return true iff the order of n0 modulo r is larger than t.
     */
    private static boolean orderExceeding(BigInteger n0, BigInteger r, int t) {
        Optional<BigInteger> findOrder = Stream.iterate(n0, n -> n.multiply(n0).mod(r)).limit(t + 1)
                .filter(n -> n.equals(BigInteger.ONE)).findFirst();
        return findOrder.isEmpty();
    }

}