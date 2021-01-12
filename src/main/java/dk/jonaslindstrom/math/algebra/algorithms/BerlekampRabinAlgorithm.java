package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRing;
import dk.jonaslindstrom.math.algebra.concretisations.PrimeField;
import dk.jonaslindstrom.math.algebra.concretisations.QuotientRing;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Find a root of a polynomial over a prime field using the probabilistic Berlekamp-Rabin algorithm.
 */
public class BerlekampRabinAlgorithm implements Function<Polynomial<Integer>, Integer> {

  private final int p;
  private final Random random;
  private final PrimeField 𝔽ₚ;
  private final PolynomialRing<Integer> 𝔽ₚx;
  private final int maxIterations;
  private final EuclideanAlgorithm<Polynomial<Integer>> gcd;

  public BerlekampRabinAlgorithm(int p, int maxIterations) {
    this.p = p;
    this.𝔽ₚ = new PrimeField(p);
    this.𝔽ₚx = new PolynomialRing<>(𝔽ₚ);
    this.random = new Random();
    this.maxIterations = maxIterations;
    this.gcd = new EuclideanAlgorithm<>(𝔽ₚx);
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
          .apply(Polynomial.of(𝔽ₚ.negate(k), 1), 𝔽ₚx);

      QuotientRing<Polynomial<Integer>> 𝔽ₚxmodf = new QuotientRing<>(𝔽ₚx, f) {
        @Override
        public int getCharacteristics() {
          return p;
        }
      };

      Polynomial<Integer> gₖ = new Power<>(𝔽ₚxmodf).apply(Polynomial.of(0, 1), (p-1) / 2);
      if (𝔽ₚx.equals(gₖ, Polynomial.constant(-1)) || 𝔽ₚx.equals(gₖ, Polynomial.constant(1))) {
        continue;
      }

      List<Polynomial<Integer>> candidates = List.of(
          𝔽ₚx.add(gₖ, 𝔽ₚx.getIdentity()),
          𝔽ₚx.subtract(gₖ, 𝔽ₚx.getIdentity()));

      for (Polynomial<Integer> candidate : candidates) {
        Polynomial<Integer> g = gcd.extendedGcd(fₖ, candidate).first;
        if (g.degree() > 0) {
          return 𝔽ₚ.subtract(apply(g), k);
        }
      }

    }
    throw new IllegalArgumentException("Exceeded max number of iterations without finding a root for the given polynomial");
  }
}
