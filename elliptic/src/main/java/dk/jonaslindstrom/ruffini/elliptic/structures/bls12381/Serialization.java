package dk.jonaslindstrom.ruffini.elliptic.structures.bls12381;

import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.integers.algorithms.ModularSquareRoot;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static dk.jonaslindstrom.ruffini.common.util.DataConversionPrimitives.I2OSP;
import static dk.jonaslindstrom.ruffini.common.util.DataConversionPrimitives.OS2IP;
import static dk.jonaslindstrom.ruffini.elliptic.structures.bls12381.Serialization.Group.G1;
import static dk.jonaslindstrom.ruffini.elliptic.structures.bls12381.Serialization.Group.G2;

public class Serialization {

    private static Map<Group, Integer> size = Map.of(G1, 2, G2, 4);
    private final boolean C, I, S;
    private final List<BigInteger> integers;
    private final Group g;

    private Serialization(boolean C, boolean I, boolean S, List<BigInteger> integers, Group g) {
        this.C = C;
        this.I = I;
        this.S = S;
        this.integers = integers;
        this.g = g;
    }

    private static Serialization fromBytes(byte[] bytes, Group g) {
        return new Serialization(
                (bytes[0] & 1 << 7) != 0,
                (bytes[0] & 1 << 6) != 0,
                (bytes[0] & 1 << 5) != 0,
                getIntegers(unmaskBytes(bytes)),
                g);
    }

    private static byte[] unmaskBytes(byte[] bytes) {
        byte[] copyOfBytes = Arrays.copyOf(bytes, bytes.length);
        copyOfBytes[0] &= 0x1F;
        return copyOfBytes;
    }

    private static Serialization fromG2Point(AffinePoint<Polynomial<BigInteger>> point) {
        if (point.isPointAtInfinity()) {
            return new Serialization(false, true, false, List.of(), G2);
        }
        return new Serialization(false, false, false, List.of(
                point.x().getCoefficient(0),
                point.x().getCoefficient(1),
                point.y().getCoefficient(0),
                point.y().getCoefficient(1)), G2);
    }

    private static Serialization fromG1Point(AffinePoint<BigInteger> point) {
        if (point.isPointAtInfinity()) {
            return new Serialization(false, true, false, List.of(), G1);
        }
        return new Serialization(false, false, false, List.of(
                point.x(),
                point.y()), G1);
    }

    private static List<BigInteger> getIntegers(byte[] bytes) {
        List<BigInteger> integers = new ArrayList<>();
        for (int i = 0; i < bytes.length; i += 48) {
            integers.add(OS2IP(Arrays.copyOfRange(bytes, i * 48, (i + 1) * 48)));
        }
        return integers;
    }

    public static byte[] serializeG1(AffinePoint<BigInteger> point, boolean compress) {
        boolean C = compress; // No compression
        boolean I = point.isPointAtInfinity();
        boolean S = compress && !I && point.y().compareTo(point.x()) > 0;

        byte m = (byte) ((C ? (1 << 7) : 0)
                + (I ? (1 << 6) : 0)
                + (S ? (1 << 5) : 0));

        int length = compress ? 48 : 96;
        byte[] s = new byte[length];

        byte[] xString = I ? new byte[48] : I2OSP(point.x(), 48);
        System.arraycopy(xString, 0, s, 0, 48);

        if (!compress) {
            byte[] yString = I ? new byte[48] : I2OSP(point.y(), 48);
            System.arraycopy(yString, 0, s, 48, 48);
        }

        s[0] |= m;
        return s;
    }

    public static AffinePoint<BigInteger> deserializeG1(byte[] bytes) {
        return Serialization.fromBytes(bytes, G1).pointAsG1();
    }

    public static AffinePoint<Polynomial<BigInteger>> deserializeG2(byte[] bytes) {
        return Serialization.fromBytes(bytes, G2).pointAsG2();
    }

    public static byte[] serializeG2(AffinePoint<Polynomial<BigInteger>> point, boolean compressed) {
        return Serialization.fromG2Point(point).getBytes(compressed);
    }

    private boolean isInfinity() {
        return I;
    }

    private boolean isCompressed() {
        return C;
    }

    private boolean yLargerThanX() {
        return S;
    }

    private byte[] getBytes(boolean compressed) {
        int integersToEncode = size.get(g);
        if (compressed) {
            integersToEncode /= 2;
        }
        byte[] buffer = new byte[integersToEncode * 48];
        for (int i = 0; i < integersToEncode; i++) {
            System.arraycopy(I2OSP(integers.get(i), 48), 0, buffer, 48 * i, 48);
        }
        buffer[0] |= (byte) ((C ? (1 << 7) : 0)
                + (I ? (1 << 6) : 0)
                + (S ? (1 << 5) : 0));
        return buffer;
    }

    private AffinePoint<BigInteger> pointAsG1() {
        if (isInfinity()) {
            return AffinePoint.pointAtInfinity();
        }
        BigInteger x = integers.get(0);
        BigInteger y;
        if (!isCompressed()) {
            y = integers.get(1);
        } else {
            ModularSquareRoot sqrt = new ModularSquareRoot(BLS12381.p);
            y = sqrt.apply(x.modPow(BigInteger.valueOf(3), BLS12381.p).add(BigInteger.valueOf(4)).mod(BLS12381.p));
            if (yLargerThanX() && y.compareTo(x) <= 0) {
                y = BLS12381.p.subtract(y);
            }
        }
        return new AffinePoint<>(x, y);
    }

    private AffinePoint<Polynomial<BigInteger>> pointAsG2() {
        if (isInfinity()) {
            return AffinePoint.pointAtInfinity();
        }
        Polynomial<BigInteger> x = Polynomial.of(BLS12381.FP, integers.get(0), integers.get(1));
        Polynomial<BigInteger> y = Polynomial.of(BLS12381.FP, integers.get(2), integers.get(3));
        return new AffinePoint<>(x, y);
    }

    public enum Group {
        G1,
        G2,
    }
}
