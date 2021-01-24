package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.util.Pair;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class PollardRho implements UnaryOperator<BigInteger> {

  private final Random random;
  private final int maxIterations;

  public PollardRho(Random random, int maxIterations) {
    this.random = random;
    this.maxIterations = maxIterations;
  }

  public PollardRho() {
    this(new Random(), 10);
  }

  private static BigInteger g(BigInteger x, BigInteger n) {
    return x.multiply(x).add(BigInteger.ONE).mod(n);
  }

  private BigInteger sample(BigInteger m) {
    BigInteger x;
    do {
      x = new BigInteger(m.bitLength(), random).mod(m);
    } while (x.compareTo(BigInteger.ZERO) == 0 || x.compareTo(m) >= 0);
    return x;
  }

  @Override
  public BigInteger apply(BigInteger m) {
    return apply(m, 0);
  }

  public BigInteger apply(BigInteger m, int i) {
    if (i > maxIterations) {
      throw new IllegalStateException("Max iterations reached");
    }

    if (m.isProbablePrime(1000)) {
      return m;
    }

    if (m.equals(BigInteger.ONE)) {
      return m;
    }

    BigInteger y = sample(m);
    BigInteger x = sample(m);

    Optional<BigInteger> d = Stream.iterate(new Pair<>(x,y), p -> new Pair<>(g(p.first,m), g(g(p.second, m), m)))
        .limit(m.sqrt().longValue()).map(p -> p.first.subtract(p.second).abs().mod(m).gcd(m))
        .filter(c -> !c.equals(BigInteger.ONE)).findAny();

    if (d.isEmpty() || d.get().equals(m)) {
      return apply(m, i + 1);
    }

    return d.get();
  }
}
