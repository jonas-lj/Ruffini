package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.exceptions.NotInvertibleException;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegersModuloN;

import java.math.BigInteger;

public class BigPrimeField extends BigIntegersModuloN implements Field<BigInteger> {

    public BigPrimeField(BigInteger p) {
        super(p);
    }

    @Override
    public BigInteger invert(BigInteger a) {
        EuclideanAlgorithm.Result<BigInteger> xgcd =
                new EuclideanAlgorithm<>(BigIntegers.getInstance()).gcd(a, super.getModulus());
        if (!xgcd.gcd().equals(BigInteger.ONE)) {
            throw new NotInvertibleException(a);
        }
        return xgcd.x().mod(super.mod);
    }

    @Override
    public String toString() {
        return "\\mathbb{F}_{" + super.mod.toString() + "}";
    }


}
