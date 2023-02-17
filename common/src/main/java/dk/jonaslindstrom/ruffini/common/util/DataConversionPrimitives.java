package dk.jonaslindstrom.ruffini.common.util;

import java.math.BigInteger;

public class DataConversionPrimitives {

    /** Implementation of Integer-to-Octet-String primitive (OS2IP) as defined in RFC 8017. */
    public static BigInteger OS2IP(byte[] bytes){
        return new BigInteger(1, bytes);
    }

    /** Implementation of Octet-String-to-Integer primitive (I2OSP) as defined in RFC 8017. */
    public static byte[] I2OSP(BigInteger x, int len) {
        byte[] bytes = x.toByteArray();

        if (bytes.length > len) {
            throw new IllegalArgumentException();
        } else if (bytes.length == len) {
            return bytes;
        } else {
            byte[] result = new byte[len];
            System.arraycopy(bytes, 0, result, len - bytes.length, bytes.length);
            return result;
        }
    }

}
