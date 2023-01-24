package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;

import java.math.BigInteger;
import java.util.Arrays;

public class Curve25519 extends MontgomeryCurve<BigInteger> {

    public static AffinePoint<BigInteger> BASE_POINT = new AffinePoint<>(BigInteger.valueOf(9),
            new BigInteger("14781619447589544791020593568409986887264606134616475288964881837755586237401"));

    public static BigInteger SUBGROUP_ORDER = BigInteger.TWO.pow(252).add(new BigInteger("27742317777372353535851937790883648493"));

    public static BigInteger P = BigInteger.TWO.pow(255).subtract(BigInteger.valueOf(19));

    public static BigInteger A = new BigInteger("486662");

    private static BigInteger A24 = new BigInteger("121665"); // = (A - 2) / 4

    public Curve25519() {
        super(new BigPrimeField(P), A, BigInteger.ONE);
    }

    public static BigInteger decodeScalar(byte[] bytes) {
        if (bytes.length != 32) {
            throw new IllegalArgumentException("Input array must contain exactly 32 bytes");
        }
        byte[] clamped = Arrays.copyOf(bytes, bytes.length);
        clamped[0] &= 248;
        clamped[31] &= 127;
        clamped[31] |= 64;

        return new BigInteger(1, reverse(clamped)).mod(P);
    }

    public static BigInteger decodePoint(byte[] bytes) {
        if (bytes.length != 32) {
            throw new IllegalArgumentException("Input array must contain exactly 32 bytes");
        }
        // Ignore unused bit
        byte[] inputBytes = Arrays.copyOf(bytes, bytes.length);
        inputBytes[31] &= 127;

        return new BigInteger(reverse(inputBytes)).mod(P);
    }

    public static byte[] encodePoint(AffinePoint<BigInteger> point) {
        return reverse(point.x().toByteArray());
    }

    /**
     * Return a copy of the given array with the entries in reversed order
     */
    private static byte[] reverse(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[i] = bytes[bytes.length - i - 1];
        }
        return reversed;
    }
}
