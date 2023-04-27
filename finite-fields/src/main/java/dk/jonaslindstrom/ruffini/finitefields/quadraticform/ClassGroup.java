package dk.jonaslindstrom.ruffini.finitefields.quadraticform;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;

import java.math.BigInteger;

/**
 * This implements the ideal class group for a negative discrimant.
 */
public class ClassGroup implements Group<QuadraticForm<BigInteger, BigIntegers>> {

    private final BigInteger discriminant;
    private final QuadraticForm<BigInteger, BigIntegers> principal;

    public ClassGroup(BigInteger discriminant) {
        if (discriminant.compareTo(BigInteger.ZERO) >= 0) {
            throw new IllegalArgumentException("Discriminant must be negative");
        }
        this.discriminant = discriminant;

        // Compute identity
        BigInteger k = discriminant.mod(BigInteger.TWO);
        this.principal =
                new QuadraticForm<>(BigIntegers.getInstance(),
                        BigInteger.ONE,
                        k,
                        k.multiply(k).subtract(discriminant).divide(BigInteger.valueOf(4)));
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> invert(QuadraticForm<BigInteger, BigIntegers> a) {
        if (!a.discriminant().equals(discriminant)) {
            throw new IllegalArgumentException("Input has wrong discriminant");
        }
        return new QuadraticForm<>(BigIntegers.getInstance(),
                a.getA(),
                a.getB().negate(),
                a.getC());
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> identity() {
        return principal;
    }

    @Override
    public QuadraticForm<BigInteger, BigIntegers> multiply(QuadraticForm<BigInteger, BigIntegers> a, QuadraticForm<BigInteger, BigIntegers> b) {
        if (!a.discriminant().equals(discriminant) || !b.discriminant().equals(discriminant)) {
            throw new IllegalArgumentException("Some of the inputs have the wrong discriminant");
        }
        return a.compose(b);
    }

    @Override
    public String toString(QuadraticForm<BigInteger, BigIntegers> a) {
        return a.toString();
    }

    @Override
    public boolean equals(QuadraticForm<BigInteger, BigIntegers> a, QuadraticForm<BigInteger, BigIntegers> b) {
        return a.getA().equals(b.getA()) && a.getB().equals(b.getB()) && a.getC().equals(b.getC());
    }
}
