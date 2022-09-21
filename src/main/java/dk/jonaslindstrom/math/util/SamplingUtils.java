package dk.jonaslindstrom.math.util;

import java.math.BigInteger;
import java.util.Random;

public class SamplingUtils {

    /** Sample a random BigInteger in the range [0,n) */
    public static BigInteger sample(BigInteger n, Random random) {
        int m = n.bitLength();
        BigInteger r = new BigInteger(m, random);
        while (r.compareTo(n) >= 0) {
            r = new BigInteger(m, random);
        }
        return r;
    }

}
