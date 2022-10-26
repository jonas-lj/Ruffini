package demo;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.BigMultiply;
import dk.jonaslindstrom.ruffini.common.exceptions.NotInvertibleException;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.ShortWeierstrassCurveAffine;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

/**
 * Implementation of the elliptic-curve factorization method (ECM). See
 * https://en.wikipedia.org/wiki/Lenstra_elliptic-curve_factorization for an explanation of the
 * algorithm.
 */
public class ECMDemo {

    public static void main(String[] arguments) {

        Random random = new Random(1234);
        BigInteger d = ECM(new BigInteger("602400691612422154516282778947806249229526581"), random);
        System.out.println(d);
    }

        public static BigInteger ECM(BigInteger n, Random random) {

            // Assume for contradiction that n is prime and ℤn is a field
            Field<BigInteger> ℤn = new BigPrimeField(n);

            int B1 = 10000;
            int iterations = 100000;

            BigInteger k = BigInteger.ONE;
            for (int i = 1; i < B1; i++) {
                k = k.multiply(BigInteger.valueOf(i).gcd(k));
            }

            for (int i = 0; i < iterations; i++) {
                BigInteger a = new BigInteger(n.bitLength(), random);
                BigInteger x = new BigInteger(n.bitLength(), random);
                BigInteger y = new BigInteger(n.bitLength(), random);
                BigInteger b = ℤn
                        .subtract(ℤn.multiply(y, y), ℤn.add(ℤn.multiply(x, x, x), ℤn.multiply(a, x)));

                BigInteger g =
                        a.pow(3).multiply(BigInteger.valueOf(4)).add(b.pow(2).multiply(BigInteger.valueOf(27))).gcd(n);
                if (g.equals(n)) {
                    continue;
                } else if (g.compareTo(BigInteger.ONE) > 0) {
                    return g;
                }

                AdditiveGroup<AffinePoint<BigInteger>> E = new ShortWeierstrassCurveAffine<>(ℤn, a, b);
                AffinePoint<BigInteger> P = new AffinePoint<>(x, y);
                BigMultiply<AffinePoint<BigInteger>> multiply = new BigMultiply<>(E);



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
