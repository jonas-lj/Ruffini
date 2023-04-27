package dk.jonaslindstrom.ruffini.integers.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.OrderedSet;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.math.BigInteger;
import java.util.Comparator;

public class BigIntegers implements EuclideanDomain<BigInteger>, OrderedSet<BigInteger> {

    private static final BigIntegers instance = new BigIntegers();

    private BigIntegers() {

    }

    public static BigIntegers getInstance() {
        return instance;
    }

    @Override
    public BigInteger identity() {
        return BigInteger.ONE;
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public String toString(BigInteger a) {
        return a.toString();
    }

    @Override
    public boolean equals(BigInteger a, BigInteger b) {
        return a.equals(b);
    }

    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger zero() {
        return BigInteger.ZERO;
    }

    @Override
    public Pair<BigInteger, BigInteger> divide(BigInteger a, BigInteger b) {
        if (b.signum() < 0) {
            Pair<BigInteger, BigInteger> qr = divide(a, b.negate());
            return new Pair<>(qr.getFirst().negate(), qr.getSecond());
        }
        BigInteger r = a.mod(b);
        BigInteger q = a.subtract(r).divide(b);
        return new Pair<>(q, r);
    }

    @Override
    public BigInteger norm(BigInteger a) {
        return a.abs();
    }

    @Override
    public String toString() {
        return "\\mathbb{Z}";
    }

    @Override
    public Comparator<BigInteger> getOrdering() {
        return BigInteger::compareTo;
    }
}
