package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.common.exceptions.InvalidParametersException;
import dk.jonaslindstrom.ruffini.common.exceptions.NotASquareException;
import dk.jonaslindstrom.ruffini.common.util.EncodingUtils;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.finitefields.algorithms.BigTonelliShanks;

import java.math.BigInteger;
import java.util.Arrays;

import static dk.jonaslindstrom.ruffini.common.util.ArrayUtils.reverse;

public class Curve25519 extends MontgomeryCurve<BigInteger, BigPrimeField> {

    public static AffinePoint<BigInteger> BASE_POINT = new AffinePoint<>(BigInteger.valueOf(9),
            new BigInteger("14781619447589544791020593568409986887264606134616475288964881837755586237401"));

    public static BigInteger SUBGROUP_ORDER = BigInteger.TWO.pow(252).add(new BigInteger("27742317777372353535851937790883648493"));

    public static BigInteger P = BigInteger.TWO.pow(255).subtract(BigInteger.valueOf(19));

    public static BigInteger A = new BigInteger("486662");

    private static BigInteger A24 = new BigInteger("121665"); // = (A - 2) / 4
    private Curve25519 curve25519;

    public Curve25519() {
        super(new BigPrimeField(P), A, BigInteger.ONE);
    }

    public static byte[] encodePoint(AffinePoint<BigInteger> point) {
        return EncodingUtils.encode(point.x(), true);
    }

    public BigInteger decodeScalar(byte[] bytes) {
        if (bytes.length != 32) {
            throw new IllegalArgumentException("Input array must contain exactly 32 bytes");
        }
        byte[] clamped = Arrays.copyOf(bytes, bytes.length);
        clamped[0] &= 248;
        clamped[31] &= 127;
        clamped[31] |= 64;

        return EncodingUtils.decode(clamped, true).mod(P);
    }

    private BigInteger decodeCoordinate(byte[] bytes) throws InvalidParametersException {
        if (bytes.length != 32) {
            throw new InvalidParametersException("Coordinates should be encoded with 32 bytes");
        }
        byte[] inputBytes = Arrays.copyOf(bytes, 32);
        // Ignore unused bit
        inputBytes[31] &= 127;
        return EncodingUtils.decode(inputBytes, true).mod(P);
    }

    public AffinePoint<BigInteger> decodePoint(byte[] bytes) throws InvalidParametersException {
        if (bytes.length == 65) {
            if (bytes[0] != 0x04) {
                throw new InvalidParametersException("The given encoding is not a valid point");
            }
            BigInteger x = decodeCoordinate(Arrays.copyOfRange(bytes, 1, 33));
            BigInteger y = decodeCoordinate(Arrays.copyOfRange(bytes, 33, 65));
            return new AffinePoint<>(x, y);
        } else if (bytes.length == 33) {
            if (bytes[0] != 0x02 && bytes[0] != 0x03) {
                throw new InvalidParametersException("The given encoding is not a valid point");
            }

            BigInteger x = decodeCoordinate(Arrays.copyOfRange(bytes, 1, 33));
            BigInteger ySquared = field.add(field.multiply(x, x, x), field.multiply(A, x, x), x);
            BigTonelliShanks squareRoot = new BigTonelliShanks((BigPrimeField) field);
            try {
                BigInteger y = squareRoot.apply(ySquared);
                return new AffinePoint<>(x, y);
            } catch (NotASquareException e) {
                throw new InvalidParametersException("The given encoding is not a valid point");
            }
        } else {
                throw new IllegalArgumentException("Input array must contain 33 or 65 bytes");
        }
    }


}
