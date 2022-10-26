package dk.jonaslindstrom.ruffini.integers.algorithms;

import dk.jonaslindstrom.ruffini.integers.algorithms.factorize.Factorize;

import java.math.BigInteger;
import java.util.List;
import java.util.function.UnaryOperator;

public class EulersTotientFunction implements UnaryOperator<BigInteger> {

    @Override
    public BigInteger apply(BigInteger n) {
        List<BigInteger> primeFactorisation = new Factorize().factor(n);
        return primeFactorisation.stream().distinct()
                .reduce(n, (i, j) -> i.multiply(j.subtract(BigInteger.ONE)).divide(j));
    }
}
