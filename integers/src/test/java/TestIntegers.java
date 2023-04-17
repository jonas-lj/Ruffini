import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;
import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class TestIntegers {

    @Test
    public void test_integers() {
        Integers integers = Integers.getInstance();
        Integer x = 3;
        Integer y = 5;

        Assert.assertEquals(Integer.valueOf(0), integers.getZero());
        Assert.assertEquals(Integer.valueOf(1), integers.getIdentity());
        Assert.assertEquals(Integer.valueOf(x + y), integers.add(x, y));
        Assert.assertEquals(Integer.valueOf(x * y), integers.multiply(x, y));
        Assert.assertEquals(Integer.valueOf(-x), integers.negate(x));
    }

    @Test
    public void test_big_integers() {
        BigIntegers integers = BigIntegers.getInstance();
        BigInteger x = BigInteger.valueOf(3);
        BigInteger y = BigInteger.valueOf(5);

        Assert.assertEquals(BigInteger.valueOf(0), integers.getZero());
        Assert.assertEquals(BigInteger.valueOf(1), integers.getIdentity());
        Assert.assertEquals(x.add(y), integers.add(x, y));
        Assert.assertEquals(x.multiply(y), integers.multiply(x, y));
        Assert.assertEquals(x.negate(), integers.negate(x));
    }

}
