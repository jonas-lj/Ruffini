package dk.jonaslindstrom.ruffini.common.algorithms;

import java.math.BigInteger;
import java.util.function.UnaryOperator;

public class BarrettReduction implements UnaryOperator<BigInteger> {

    private final BigInteger m;
    private final int k;
    private final BigInteger r;

    public BarrettReduction(BigInteger modulus) {

        if (modulus.compareTo(BigInteger.valueOf(3)) <= 0) {
            throw new IllegalArgumentException("Modulus must be greater than 3");
        }

        if (modulus.bitCount() == 1) {
            throw new IllegalArgumentException("Modulus cannot be a power of two");
        }

        this.m = modulus;
        this.k = modulus.bitLength();
        this.r = BigInteger.ONE.shiftLeft(2 * k).divide(modulus);
    }

    private BigInteger modPositive(BigInteger x) {
        BigInteger t = x.subtract(x.multiply(r).shiftRight(2 * k).multiply(m));

        if (t.compareTo(m) < 0) {
            return t;
        } else {
            return t.subtract(m);
        }
    }

    @Override
    public BigInteger apply(BigInteger x) {
        if (x.signum() >= 0) {
            return modPositive(x);
        } else {
            return m.subtract(modPositive(x.abs()));
        }
    }

}
