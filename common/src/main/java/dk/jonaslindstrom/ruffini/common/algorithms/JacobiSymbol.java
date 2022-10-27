package dk.jonaslindstrom.ruffini.common.algorithms;

import java.util.function.IntBinaryOperator;

public class JacobiSymbol implements IntBinaryOperator {

    @Override
    public int applyAsInt(int k, int n) {
        if (k <= 0 || Math.floorMod(k, 2) == 0) {
            throw new IllegalArgumentException("n must be a positive odd number but was " + k + ".");
        }

        n = Math.floorMod(n, k);
        int t = 1;

        while (n != 0) {
            while (Math.floorMod(n, 2) == 0) {
                n = n >> 1;
                int r = Math.floorMod(k, 8);
                if (r == 3 || r == 5) {
                    t = -t;
                }
            }

            // Swap n and k
            int tmp = n;
            n = k;
            k = tmp;

            if (Math.floorMod(n, 4) == 3 && Math.floorMod(k, 4) == 3) {
                t = -t;
            }
            n = Math.floorMod(n, k);
        }

        return k == 1 ? t : 0;
    }

}
