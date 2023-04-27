package dk.jonaslindstrom.ruffini.integers.algorithms;

import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.integers.structures.BigIntegers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CongruenceSolver {

    private final BigInteger m;

    public CongruenceSolver(BigInteger m) {
        this.m = m;
    }

    /** Solve p_i x = q_i (mod m) for x assuming gcd(m, p_1, ..., p_r) = 1 and p_i q_j = q_j p_i (mod m) for all i,j. */
    public BigInteger solve(List<BigInteger> p, List<BigInteger> q) {
        if (p.size() != q.size()) {
            throw new IllegalArgumentException("p and q must have the same size.");
        }
        int r = p.size();

        EuclideanAlgorithm<BigInteger> euclideanAlgorithm = new EuclideanAlgorithm<>(BigIntegers.getInstance());
        List<BigInteger> inputs = new ArrayList<>();
        inputs.add(m);
        inputs.addAll(p);
        EuclideanAlgorithm.ExtendedResult<BigInteger> xgcd = euclideanAlgorithm.gcd(inputs);
        if (!xgcd.gcd().equals(BigInteger.ONE)) {
            throw new IllegalArgumentException("m and p_1, ..., p_r are not pairwise co-prime.");
        }

        List<BigInteger> coefficients = xgcd.bezout();
        return IntStream.range(0, r).mapToObj(i -> q.get(i).multiply(coefficients.get(i)).mod(m)).reduce(BigInteger.ZERO, BigInteger::add).mod(m);
    }

}
