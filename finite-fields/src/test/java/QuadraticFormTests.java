import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.finitefields.quadraticform.ClassGroup;
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

    @Test
    public void testGroup() {
        ClassGroup group = new ClassGroup(BigInteger.valueOf(-23));

        QuadraticForm<BigInteger, BigIntegers> P = new QuadraticForm<>(
                BigIntegers.getInstance(),
                BigInteger.valueOf(2),
                BigInteger.valueOf(-1),
                BigInteger.valueOf(3));

        Assert.assertEquals(P.discriminant(), BigInteger.valueOf(-23));

        Assert.assertNotEquals(P.reduce(), group.identity());

        Assert.assertEquals(group.identity(), group.multiply(P, group.invert(P)));

        // The class number is 3
        Assert.assertEquals(group.identity(), group.multiply(P, P, P));
    }

    @Test
    public void testLargeGroup() {
        BigInteger d = BigInteger.valueOf(-251); // Class number is 7
        ClassGroup group = new ClassGroup(d);

        QuadraticForm<BigInteger, BigIntegers> P = new QuadraticForm<>(
                BigIntegers.getInstance(),
                BigInteger.valueOf(5),
                BigInteger.valueOf(-3),
                BigInteger.valueOf(13));

        Assert.assertEquals(P.discriminant(), d);
        Assert.assertNotEquals(P.reduce(), group.identity());

        Power<QuadraticForm<BigInteger, BigIntegers>> power = new Power<>(group);
        for (int i = 1; i < 7; i++) {
            Assert.assertNotEquals(group.identity(), power.apply(P, i));
        }
        Assert.assertEquals(group.identity(), power.apply(P, 7));
    }
}
