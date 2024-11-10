package dk.jonaslindstrom.ruffini.common.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SamplingUtils {

    /**
     * Sample a random BigInteger in the range [0,n)
     */
    public static BigInteger sample(BigInteger n, Random random) {
        int m = n.bitLength();
        BigInteger r = new BigInteger(m, random);
        while (r.compareTo(n) >= 0) {
            r = new BigInteger(m, random);
        }
        return r;
    }

    public static Optional<Pair<BigInteger, List<BigInteger>>> sampleFactoredNumber(BigInteger upperBound, Random random) {
        List<BigInteger> s = generateDecreasingSequence(upperBound, random);
        List<BigInteger> r = s.stream().filter(si -> si.isProbablePrime(30)).toList();
        BigInteger result = r.stream().reduce(BigInteger.ONE, BigInteger::multiply);

        boolean biasCorrected = random.nextDouble() < result.doubleValue() / upperBound.doubleValue();
        if (result.compareTo(upperBound) > 0 || !biasCorrected) {
            return Optional.empty();
        } else {
            return Optional.of(Pair.of(result, r));
        }
    }

    /**
     * Sample a random BigInteger in the range [0,n]
     */
    public static BigInteger sampleInclusive(BigInteger n, Random random) {
        return sample(n.add(BigInteger.ONE), random);
    }

    /**
     * Generate a decreasing list of numbers s_0, s_1, ..., s_k such that s_0 \leq n and s_k = 1.
     */
    public static List<BigInteger> generateDecreasingSequence(BigInteger n, Random random) {
        List<BigInteger> result = new ArrayList<>();
        while (n.compareTo(BigInteger.ONE) > 0) {
            n = sampleInclusive(n, random);
            result.add(n);
        }
        return result;
    }

}
