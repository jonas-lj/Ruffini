package dk.jonaslindstrom.ruffini.integers.algorithms.factorize;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Factorize {

    private final UnaryOperator<BigInteger> factorisation;

    public Factorize(UnaryOperator<BigInteger> algorithm) {
        this.factorisation = algorithm;
    }

    public Factorize() {
        this(new PollardRho());
    }

    public List<BigInteger> factor(BigInteger n) {

        if (n.isProbablePrime(1000)) {
            return List.of(n);
        }

        List<BigInteger> composites = new LinkedList<>();
        List<BigInteger> factors = new LinkedList<>();
        composites.add(n);

        do {
            List<BigInteger> newFactors = composites.stream().map(
                    this::factorInternal).flatMap(List::stream).collect(Collectors.toList());
            List<BigInteger> primes = newFactors.stream().filter(m -> m.isProbablePrime(1000)).toList();
            newFactors.removeAll(primes);
            factors.addAll(primes);
            composites = newFactors;
        } while (!composites.isEmpty());

        return factors;
    }

    private List<BigInteger> factorInternal(BigInteger m) {

        BigInteger d = factorisation.apply(m);

        if (d.equals(BigInteger.ONE) || d.equals(m)) {
            return List.of(m);
        }

        return List.of(d, m.divide(d));
    }

}
