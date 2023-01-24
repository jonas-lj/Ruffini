import dk.jonaslindstrom.ruffini.common.exceptions.InvalidParametersException;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.Curve25519;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class TestCurve25519 {

    @Test
    public void testPointMultiplication() throws DecoderException, InvalidParametersException {
        Curve25519 curve25519 = new Curve25519();
        BigInteger n = curve25519.decodeScalar(Hex.decodeHex("a8abababababababababababababababababababababababababababababab6b"));
        AffinePoint<BigInteger> x = curve25519.scale(n, Curve25519.BASE_POINT);
        AffinePoint<BigInteger> expected = curve25519.decodePoint(Hex.decodeHex("e3712d851a0e5d79b831c5e34ab22b41a198171de209b8b8faca23a11c624859"));
        Assert.assertEquals(expected.x(), x.x());

        n = curve25519.decodeScalar(Hex.decodeHex("c8cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd4d"));
        x = curve25519.scale(n, Curve25519.BASE_POINT);
        expected = curve25519.decodePoint(Hex.decodeHex("b5bea823d9c9ff576091c54b7c596c0ae296884f0e150290e88455d7fba6126f"));
        Assert.assertEquals(expected.x(), x.x());
    }

}
