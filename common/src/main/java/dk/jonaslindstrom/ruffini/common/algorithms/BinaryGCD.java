package dk.jonaslindstrom.ruffini.common.algorithms;

import java.math.BigInteger;

public class BinaryGCD {

    public static BigInteger apply(BigInteger x, BigInteger y) {
        int beta = Math.min(twoValence(x), twoValence(y));
        x = x.shiftRight(beta);
        y = y.shiftRight(beta);

        while (!y.equals(x)) {
            BigInteger z = x.min(y);
            BigInteger d = x.subtract(y).abs();
            y = d.shiftRight(twoValence(d));
            x = z;
        }
        return x.shiftLeft(beta);
    }

    private static int twoValence(BigInteger x) {
        return x.getLowestSetBit();
    }

}
