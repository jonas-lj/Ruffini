package dk.jonaslindstrom.ruffini.finitefields.algorithms;

import dk.jonaslindstrom.ruffini.common.algorithms.BigLegendreSymbol;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.exceptions.NotASquareException;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;

import java.math.BigInteger;

public class BigTonelliShanks {

    private final BigPrimeField field;
    private final Power<BigInteger> power;

    public BigTonelliShanks(BigPrimeField field) {
        this.field = field;
        this.power = new Power<>(field);
    }

    public BigInteger apply(BigInteger a) throws NotASquareException  {

        // Algorithm 1.5.1 in "A Course in Computational Algebraic Number Theory", Cohen (1993)

        BigLegendreSymbol legendreSymbol = new BigLegendreSymbol(field.getModulus());
        if (legendreSymbol.apply(a) != 1) {
            throw new NotASquareException();
        }

        EvenOddPart evenOdd = EvenOddPart.from(field.getModulus().subtract(BigInteger.ONE));
        BigInteger q = evenOdd.odd;
        int r = evenOdd.even; // r >= 1

        BigInteger n = BigInteger.TWO;
        while (legendreSymbol.apply(n) > -1) {
            n = n.add(BigInteger.ONE);
        }

        BigInteger y = power.apply(n, q);
        BigInteger x = power.apply(a, q.subtract(BigInteger.ONE).shiftRight(1));
        BigInteger b = field.multiply(a, x, x);
        x = field.multiply(a, x);

        while (!field.isIdentity(b)) {
            int m = 1;
            if (r == 1) {
                throw new NotASquareException();
            }

            while (!field.isIdentity(power.apply(b, 1 << m))) {
                m += 1;
                if (m == r) {
                    throw new NotASquareException();
                }
            }

            BigInteger t = power.apply(y, 1 << (r-m-1));
            y = field.multiply(t, t);
            r = m;
            x = field.multiply(t, x);
            b = field.multiply(b, y);
        }
        return x;
    }

    /** Write a number <i>n</i> as <i>n = 2^even * odd</i>. */
    private record EvenOddPart(int even, BigInteger odd) {

        private static EvenOddPart from(BigInteger n) {
            BigInteger odd = n;
            int even = 0;

            while (odd.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                odd = odd.shiftRight(1);
                even++;
            }
            return new EvenOddPart(even, odd);
        }

    }

}
