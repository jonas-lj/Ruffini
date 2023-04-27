import dk.jonaslindstrom.ruffini.finitefields.quadraticform.QuadraticForm;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class QuadraticFormTests {

    @Test
    public void testReduction() {
        QuadraticForm<BigInteger, BigIntegers> P = new QuadraticForm<>(
                BigIntegers.getInstance(),
                BigInteger.valueOf(11),
                BigInteger.valueOf(49),
                BigInteger.valueOf(55));

        QuadraticForm<BigInteger, BigIntegers> Q = P.reduce();
        System.out.println(Q);
        Assert.assertEquals(BigInteger.valueOf(1), Q.getA());
        Assert.assertEquals(BigInteger.valueOf(1), Q.getB());
        Assert.assertEquals(BigInteger.valueOf(5), Q.getC());
    }

    @Test
    public void testComposition() {

        QuadraticForm<BigInteger, BigIntegers> P = new QuadraticForm<>(
                BigIntegers.getInstance(),
                BigInteger.valueOf(14),
                BigInteger.valueOf(10),
                BigInteger.valueOf(21));
        QuadraticForm<BigInteger, BigIntegers> Q = new QuadraticForm<>(
                BigIntegers.getInstance(),
                BigInteger.valueOf(9),
                BigInteger.valueOf(2),
                BigInteger.valueOf(30));

        QuadraticForm<BigInteger, BigIntegers> PQ = P.compose(Q);

        System.out.println(PQ);
        System.out.println(PQ.reduce());
    }

}
