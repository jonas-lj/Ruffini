package dk.jonaslindstrom.ruffini.common.util;

import java.math.BigInteger;

import static dk.jonaslindstrom.ruffini.common.util.ArrayUtils.reverse;

public class EncodingUtils {

    public static byte[] encode(BigInteger x, boolean littleEndian) {
        byte[] bytes = x.toByteArray();
        if (littleEndian) {
            bytes = reverse(bytes);
        }
        return bytes;
    }

    public static BigInteger decode(byte[] bytes, boolean littleEndian) {
        if (littleEndian) {
            bytes = reverse(bytes);
        }
        return new BigInteger(1, bytes);
    }

}
