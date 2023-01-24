package dk.jonaslindstrom.ruffini.finitefields.algorithms;

import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.structures.QuotientRing;
import dk.jonaslindstrom.ruffini.finitefields.PrimeField;
import dk.jonaslindstrom.ruffini.integers.IntegerPolynomial;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Find a root of a polynomial over a prime field using the probabilistic Berlekamp-Rabin
 * algorithm.
 */
public class BerlekampRabinAlgorithm implements Function<Polynomial<Integer>, Integer> {

    private final int p;
    private final Random random;
    private final PrimeField 𝔽ₚ;
    private final PolynomialRing<Integer> 𝔽ₚx;
    private final int maxIterations;
    private final EuclideanAlgorithm<Polynomial<Integer>> gcd;

    public BerlekampRabinAlgorithm(int p, int maxIterations, Random random) {
        this.p = p;
        this.𝔽ₚ = new PrimeField(p);
        this.𝔽ₚx = new PolynomialRing<>(𝔽ₚ);
        this.random = random;
        this.maxIterations = maxIterations;
        this.gcd = new EuclideanAlgorithm<>(𝔽ₚx);
    }

    public BerlekampRabinAlgorithm(int p, int maxIterations, int seed) {
        this(p, maxIterations, new Random(seed));
    }

    public BerlekampRabinAlgorithm(int p, int maxIterations) {
        this(p, maxIterations, new Random());
    }

    public BerlekampRabinAlgorithm(int p) {
        this(p, 100);
    }

    @Override
    public Integer apply(Polynomial<Integer> f) {

        if (f.degree() == 1) {
            int x0 = 𝔽ₚ.negate(f.getCoefficient(0));
            return 𝔽ₚ.divide(x0, f.getCoefficient(1));
        }

        for (int i = 0; i < maxIterations; i++) {
            int k = random.nextInt(p - 1) + 1;
            if (f.apply(k, 𝔽ₚ) == 0) {
                return k;
            }

            // fₖ = f(x - k)
            Polynomial<Integer> fₖ = f.mapCoefficients(Polynomial::constant)
                    .apply(IntegerPolynomial.of(𝔽ₚ.negate(k), 1), 𝔽ₚx);

            QuotientRing<Polynomial<Integer>> 𝔽ₚxmodf = new QuotientRing<>(𝔽ₚx, f);

            Polynomial<Integer> gₖ = new Power<>(𝔽ₚxmodf).apply(IntegerPolynomial.of(0, 1), (p - 1) / 2);
            if (𝔽ₚx.equals(gₖ, Polynomial.constant(-1)) || 𝔽ₚx.equals(gₖ, Polynomial.constant(1))) {
                continue;
            }

            List<Polynomial<Integer>> candidates = List.of(
                    𝔽ₚx.add(gₖ, 𝔽ₚx.getIdentity()),
                    𝔽ₚx.subtract(gₖ, 𝔽ₚx.getIdentity()));

            for (Polynomial<Integer> candidate : candidates) {
                Polynomial<Integer> g = gcd.extendedGcd(fₖ, candidate).getFirst();
                if (g.degree() > 0) {
                    return 𝔽ₚ.subtract(apply(g), k);
                }
            }

        }
        throw new IllegalArgumentException(
                "Exceeded max number of iterations without finding a root for the given polynomial");
    }
}
