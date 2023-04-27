package dk.jonaslindstrom.ruffini.common.util;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;

import java.math.BigInteger;
import java.util.Objects;

public class TestUtils {
    public static class TestIntegers implements EuclideanDomain<Integer> {

        @Override
        public Integer negate(Integer a) {
            return -a;
        }

        @Override
        public Integer add(Integer a, Integer b) {
            return a + b;
        }

        @Override
        public Integer zero() {
            return 0;
        }

        @Override
        public Integer identity() {
            return 1;
        }

        @Override
        public Integer multiply(Integer a, Integer b) {
            return a * b;
        }

        @Override
        public String toString(Integer a) {
            return a.toString();
        }

        @Override
        public boolean equals(Integer a, Integer b) {
            return Objects.equals(a, b);
        }

        @Override
        public Pair<Integer, Integer> divide(Integer a, Integer b) {
            int q = Math.floorDiv(a, b);
            int r = a - q * b;
            return Pair.of(q, r);
        }

        @Override
        public BigInteger norm(Integer a) {
            return BigInteger.valueOf(a >= 0 ? a : -a);
        }
    }

    public static class TestBigIntegers implements EuclideanDomain<BigInteger> {

        @Override
        public BigInteger negate(BigInteger a) {
            return a.negate();
        }

        @Override
        public BigInteger add(BigInteger a, BigInteger b) {
            return a.add(b);
        }

        @Override
        public BigInteger zero() {
            return BigInteger.ZERO;
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
        public Pair<BigInteger, BigInteger> divide(BigInteger a, BigInteger b) {
            BigInteger[] qAndR = a.divideAndRemainder(b);

            if (qAndR[1].signum() == -1) {
                return new Pair<>(qAndR[0].add(BigInteger.ONE), qAndR[1].add(b));
            }
            return new Pair<>(qAndR[0], qAndR[1]);
        }

        @Override
        public BigInteger norm(BigInteger a) {
            return a.abs();
        }
    }

}
