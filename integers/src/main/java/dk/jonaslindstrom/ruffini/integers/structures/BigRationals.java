package dk.jonaslindstrom.ruffini.integers.structures;

import dk.jonaslindstrom.ruffini.common.structures.FieldOfFractions;

import java.math.BigInteger;

public class BigRationals extends FieldOfFractions<BigInteger> {

    private static final BigRationals instance = new BigRationals();

    private BigRationals() {
        super(BigIntegers.getInstance());
    }

    public static BigRationals getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "\\mathbb{Q}";
    }

}
