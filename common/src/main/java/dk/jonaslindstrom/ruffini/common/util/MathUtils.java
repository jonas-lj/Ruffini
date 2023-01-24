package dk.jonaslindstrom.ruffini.common.util;

import java.util.Arrays;
import java.util.Random;

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
}
