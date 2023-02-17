package dk.jonaslindstrom.ruffini.common.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathUtils {

    public static int product(int... factors) {
        return Arrays.stream(factors).reduce(1, (a, b) -> a * b);
    }

    public static int nextPowerOfTwo(int n) {
        n--;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n++;
        return n;
    }

    public static List<Integer> binaryExpansion(BigInteger x) {
        List<Integer> expansion = new ArrayList<>();
        while (!x.equals(BigInteger.ZERO)) {
            expansion.add(x.mod(BigInteger.TWO).intValue());
            x = x.shiftRight(1);
        }
        return expansion;
    }
}
