import dk.jonaslindstrom.ruffini.common.exceptions.NotASquareException;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.finitefields.algorithms.BigTonelliShanks;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class AlgorithmTests {

    @Test
    public void testSquareRoot() throws NotASquareException {
        // p = 1 (mod 8)
        BigInteger p = BigInteger.valueOf(17);
        BigInteger a = BigInteger.valueOf(13);

        BigPrimeField field = new BigPrimeField(p);
        BigTonelliShanks squareRoot = new BigTonelliShanks(field);
        BigInteger x = squareRoot.apply(a);

        Assert.assertEquals(a, field.multiply(x, x));
    }

}
