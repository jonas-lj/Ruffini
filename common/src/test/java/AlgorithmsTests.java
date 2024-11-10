import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.*;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.util.TestUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static dk.jonaslindstrom.ruffini.common.util.SamplingUtils.generateDecreasingSequence;
import static dk.jonaslindstrom.ruffini.common.util.SamplingUtils.sampleFactoredNumber;

public class AlgorithmsTests {

    @Test
    public void testBigMultiply() {
        AdditiveGroup<BigInteger> integers = new TestUtils.TestBigIntegers();
        Random random = new Random(1234);
        int tests = 100;
        for (int i = 0; i < tests; i++) {
            BigInteger x = new BigInteger(64, random);
            BigInteger s = new BigInteger(64, random);
            BigInteger result = new Multiply<>(integers).apply(s, x);
            BigInteger expected = s.multiply(x);
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testMultiply() {
        AdditiveGroup<Integer> integers = new TestUtils.TestIntegers();
        Random random = new Random(1234);
        int tests = 100;
        for (int i = 0; i < tests; i++) {
            int x = random.nextInt(1 << 16);
            int s = random.nextInt(1 << 16);
            int result = new Multiply<>(integers).apply(s, x);
            int expected = s * x;
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testBigPower() {
        Ring<BigInteger> integers = new TestUtils.TestBigIntegers();
        Random random = new Random(1234);
        int tests = 10;
        for (int i = 0; i < tests; i++) {
            BigInteger x = new BigInteger(16, random);
            int s = random.nextInt(1 << 16);
            BigInteger result = new Power<>(integers).apply(x, s);
            BigInteger expected = x.pow(s);
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testPower() {
        Ring<Integer> integers = new TestUtils.TestIntegers();
        Random random = new Random(1234);
        int tests = 100;
        for (int i = 0; i < tests; i++) {
            int x = random.nextInt(8);
            int s = random.nextInt(8);
            int result = new Power<>(integers).apply(s, x);
            int expected = BigInteger.valueOf(s).pow(x).intValueExact();
            Assert.assertEquals(expected, result);
        }
    }

    @Test
    public void testChineseRemainderTheorem() {
        EuclideanDomain<BigInteger> integers = new TestUtils.TestBigIntegers();
        Random random = new Random(1234);
        int testSets = 10;
        int testsPerSet = 10;
        for (int i = 0; i < testSets; i++) {

            // Number of bases
            int n = random.nextInt(6) + 4;

            // Generate random pseudo-prime bases
            List<BigInteger> bases = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                BigInteger base = BigInteger.probablePrime(16, random);
                if (bases.contains(base)) {
                    continue;
                }
                bases.add(base);
            }

            // The combined modulus is the product of the bases
            BigInteger m = new Product<>(integers).apply(bases);

            for (int k = 0; k < testsPerSet; k++) {
                BigInteger x = SamplingUtils.sample(m, random);
                List<BigInteger> a = bases.stream().map(x::mod).toList();
                BigInteger actual = new ChineseRemainderTheorem<>(integers).apply(Vector.ofList(a), Vector.ofList(bases));
                Assert.assertEquals(x, actual);
            }
        }
    }

    @Test
    public void testSum() {
        Ring<BigInteger> integers = new TestUtils.TestBigIntegers();
        Random random = new Random(1234);
        int tests = 100;
        for (int i = 0; i < tests; i++) {
            List<BigInteger> terms = new ArrayList<>();
            BigInteger x = new BigInteger(16, random);
            terms.add(random.nextBoolean() ? x : x.negate());
            BigInteger result = new Sum<>(integers).apply(terms);
            Assert.assertEquals(terms.stream().reduce(BigInteger::add).orElse(BigInteger.ZERO), result);
        }
    }

    @Test
    public void testEuclideanAlgorithm() {
        EuclideanDomain<BigInteger> integers = new TestUtils.TestBigIntegers();
        Random random = new Random(1234);

        int tests = 100;
        for (int i = 0; i < tests; i++) {
            BigInteger a = new BigInteger(16, random);
            BigInteger b = new BigInteger(16, random);
            EuclideanAlgorithm.Result<BigInteger> gcd = new EuclideanAlgorithm<>(integers).applyExtended(a, b);
            Assert.assertEquals(a.gcd(b), gcd.gcd());
            Assert.assertEquals(gcd.gcd(), gcd.x().multiply(a).add(gcd.y().multiply(b)));
        }

        for (int i = 0; i < tests; i++) {
            BigInteger a = new BigInteger(16, random);
            BigInteger b = new BigInteger(16, random);
            BigInteger c = new BigInteger(16, random);
            EuclideanAlgorithm.ExtendedResult<BigInteger> gcd = new EuclideanAlgorithm<>(integers).applyExtended(a, b, c);
            Assert.assertEquals(a.gcd(b).gcd(c), gcd.gcd());
            Assert.assertEquals(gcd.gcd(), gcd.bezout().get(0).multiply(a).add(gcd.bezout().get(1).multiply(b)).add(gcd.bezout().get(2).multiply(c)));
        }
    }

    @Test
    public void testBinaryGCD() {
        Random random = new Random(1234);

        int tests = 100;
        for (int i = 0; i < tests; i++) {
            BigInteger a = new BigInteger(16, random);
            BigInteger b = new BigInteger(16, random);
            BigInteger gcd = BinaryGCD.apply(a, b);
            Assert.assertEquals(a.gcd(b), gcd);
        }
    }


    @Test
    public void countQuadraticResidues() {
        int n = 1000001;
        // 1000001 = 101 * 9901
        int c = 0;
        for (int i = 1; i < n; i++) {
            int j = new JacobiSymbol().applyAsInt(i, n);
        }
    }

    @Test
    public void testDecreasingSequence() {
        Random random = new Random(1234);
        BigInteger upperBound = BigInteger.valueOf(100);
        List<BigInteger> result = generateDecreasingSequence(upperBound, random);
        System.out.println(result);
    }

    @Test
    public void testSampleFactored() {
        Random random = new Random(1234);
        BigInteger upperBound = BigInteger.valueOf(100000);

        for (int i = 0; i < 1000; i++) {
            Optional<Pair<BigInteger, List<BigInteger>>> result = sampleFactoredNumber(upperBound, random);
            result.ifPresent(System.out::println);
        }

    }
}
