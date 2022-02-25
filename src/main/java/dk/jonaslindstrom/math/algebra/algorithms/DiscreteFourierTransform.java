package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.vector.ConcreteVector;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Compute the Discrete Fourier Transform over a ring.
 */
public class DiscreteFourierTransform<E> implements UnaryOperator<Vector<E>> {

  private final Ring<E> ring;
  private final E a;
  private final int n;

  public DiscreteFourierTransform(Ring<E> ring, E nThPrincipalRootOfUnity, int n) {
    this.ring = ring;
    this.a = nThPrincipalRootOfUnity;
    this.n = n;
  }

  @Override
  public Vector<E> apply(Vector<E> x) {
    return new FDFT<>(ring, a, n).apply(x);
  }

  private static class DFT<F> implements UnaryOperator<Vector<F>> {

    private final Ring<F> ring;
    private final F a;
    private final int n;

    public DFT(Ring<F> ring, F a, int n) {
      this.ring = ring;
      this.a = a;
      this.n = n;
    }

    @Override
    public Vector<F> apply(Vector<F> v) {
      assert (v.getDimension() == n);
      Power<F> power = new Power<>(ring);
      Sum<F> sum = new Sum<>(ring);

      return new ConcreteVector<>(n,
          k -> sum.apply(j -> ring.multiply(v.get(j), power.apply(a, j * k)), n));
    }
  }

  private static class FDFT<F> implements UnaryOperator<Vector<F>> {

    private final Ring<F> ring;
    private final F a;
    private final int n;

    public FDFT(Ring<F> ring, F a, int n) {
      this.ring = ring;
      this.a = a;
      this.n = n;
    }

    @Override
    public Vector<F> apply(Vector<F> x) {
      assert (x.getDimension() == n);

      if (n % 2 != 0) {
        if (n == 1) {
          return Vector.of(x.get(0));
        } else {
          return new DFT<>(ring, a, n).apply(x);
        }
      }

      Power<F> power = new Power<>(ring);
      FDFT<F> fntt = new FDFT<>(ring, ring.multiply(a, a), n / 2);

      Vector<F> even = fntt.apply(Vector.view(n / 2, i -> x.get(2 * i)));
      Vector<F> odd = fntt.apply(Vector.view(n / 2, i -> x.get(2 * i + 1)));

      ArrayList<F> Y = new ArrayList<>();
      Y.addAll(Collections.nCopies(n, null));

      IntStream.range(0, n / 2).parallel().forEach(k -> {
        F ak = ring.multiply(power.apply(a, k), odd.get(k));
        Y.set(k, ring.add(even.get(k), ak));
        Y.set(k + n/2, ring.subtract(even.get(k), ak));
      });

      return Vector.ofList(Y);
    }

  }

}
