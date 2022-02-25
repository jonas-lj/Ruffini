package dk.jonaslindstrom.math.demo;

import static dk.jonaslindstrom.math.util.ArrayUtils.nonEmptySubsets;
import static dk.jonaslindstrom.math.util.ArrayUtils.removeElements;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.math.algebra.algorithms.MatrixMultiplication;
import dk.jonaslindstrom.math.algebra.algorithms.Sum;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import dk.jonaslindstrom.math.algebra.concretisations.MatrixRing;
import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRingOverRing;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.Polynomial.Builder;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.util.ArrayUtils;
import dk.jonaslindstrom.math.util.SamePair;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Find an Hadamard Matrix of order 92 using Williamson's method as presented in J.S. Wallis,
 * "Construction of Williamson type matrices" Linear and Multilinear Algebra , 3 (1975) pp. 197–207).
 */
public class HadamardMatrix {

  public static void main(String[] arguments) {

    Instant start = Instant.now();

    // We are searching for a Hadamard matrix of size 92 = 4x23
    int n = 23;

    // 4n written as a sum of four odd squares
    int[] k = {9, 3, 1, 1};
    assert (Arrays.stream(k).map(x -> x * x).sum() == 4 * n);

    // Compute mi's such that ki^2 = (1 + 4mi)^2
    int[] m = Arrays.stream(k).map(HadamardMatrix::computeM).toArray();

    // Define structures used for computation
    Ring<Polynomial<Integer>> ring = new FormalRing<>(n, Integers.getInstance());
    Sum<Polynomial<Integer>> sum = new Sum<>(ring);

    // Find all partitions of 0,..., (n-1)/2 - 1 into four non-empty sets
    int[] indices = IntStream.range(0, (n - 1) / 2).toArray();
    Stream<List<int[]>> inFour = partition(indices, 4).parallel();

    // Filter out any partitions where part i cannot be divided into two such that #Pi - #Ni = mi
    for (int i = 0; i < 4; i++) {
      int finalI = i;
      inFour = inFour.filter(p -> p.get(finalI).length >= Math.abs(m[finalI]));
      inFour = inFour
          .filter(p -> Math.floorMod(p.get(finalI).length, 2) == Math.floorMod(m[finalI], 2));
    }

    // Create a stream of all possible divisions
    Stream<List<SamePair<int[]>>> paired = inFour.flatMap(p -> {
      List<SamePair<int[]>> p0 = divide(p.get(0), m[0]);
      List<SamePair<int[]>> p1 = divide(p.get(1), m[1]);
      List<SamePair<int[]>> p2 = divide(p.get(2), m[2]);
      List<SamePair<int[]>> p3 = divide(p.get(3), m[3]);
      return p0.stream().flatMap(p0i -> p1.stream().flatMap(p1i -> p2.stream().flatMap(
          p2i -> p3.stream().map(p3i -> List.of(p0i, p1i, p2i, p3i)))));
    });

    // For each partition, create the corresponding polynomials
    List<Polynomial<Integer>> Vj = computeVjs(n);
    Stream<List<Polynomial<Integer>>> candidates = paired.parallel().map(partition ->
        partition.parallelStream().map(pn -> {
          List<Polynomial<Integer>> Pi = ArrayUtils.sublist(Vj, pn.first);
          List<Polynomial<Integer>> Ni = ArrayUtils.sublist(Vj, pn.second);
          return ring.subtract(ring.add(ring.getIdentity(), sum.apply(Pi)), sum.apply(Ni));
        }).collect(Collectors.toList())
    );

    // Find a set of polynomials of type III
    Polynomial<Integer> target = Polynomial.constant(4 * n);
    Stream<List<Polynomial<Integer>>> result = candidates.parallel()
        .filter(B -> ring.equals(sum.apply(B.stream().map(
            Bi -> ring.multiply(Bi, Bi)).collect(Collectors.toList())), target));
    Optional<List<Polynomial<Integer>>> B = result.findAny();

    if (B.isEmpty()) {
      System.out.println("Unable to find Hadamard matrix of order " + n);
      return;
    }

    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);
    System.out.println(B.get());
    System.out.println("Took " + duration);

    // Construct polynomials of type I from result
    List<Polynomial<Integer>> A = typeIIItoTypeI(B.get(), n);

    // Build Hadamard matrix from polynomials of type I
    Matrix<Integer> H = hadamardMatrixFromTypeI(A, n);

    // Output result and check that it is a Hadamard matrix
    System.out.println(H);
    assert (new MatrixMultiplication<>(Integers.getInstance()).apply(H, H.transpose())
        .equals(Matrix.of(4 * n, 4 * n, (i, j) -> i.equals(j) ? 4 * n : 0)));

  }

  /**
   * Find all partitions of a set into k parts. Note that symmetric cases are given, eg [[1],[2,3]]
   * and [[2,3], [1]] are both given as a partition.
   */
  private static Stream<List<int[]>> partition(int[] set, int k) {

    if (set.length == 0) {
      return Stream.of();
    }

    if (k == 1) {
      return Stream.of(List.of(set));
    }

    return nonEmptySubsets(set).flatMap(s -> {
      int[] remaining = removeElements(set, s);
      Stream<List<int[]>> partitions = partition(remaining, k - 1);
      return partitions.map(p -> {
        LinkedList<int[]> full = new LinkedList<>(p);
        full.addFirst(s);
        return full;
      });
    });
  }

  /** List all possible ways to divide the given set into two sets A and B such that #A - #B = d. */
  private static List<SamePair<int[]>> divide(int[] set, int d) {
    assert (set.length >= Math.abs(d));
    assert (Math.floorMod(set.length, 2) == Math.floorMod(d, 2));

    int l = (set.length + d) / 2;
    Stream<int[]> left = ArrayUtils.ksubsets(l, set);
    return left.map(subset -> new SamePair<>(subset, ArrayUtils.removeElements(set, subset)))
        .collect(
            Collectors.toList());
  }

  /** Compute <i>v<sub>j</sub> = u<sup>j</sup> + u<sup>n-j</sup></i> for <i>j = 1, ..., (n-1) / 2</i>. */
  private static List<Polynomial<Integer>> computeVjs(int n) {
    return IntStream.rangeClosed(1, (n - 1) / 2).mapToObj(j ->
        new Polynomial.Builder<>(Integers.getInstance()).set(j, 2).set(n - j, 2).build()).collect(
        Collectors.toList());
  }

  /** Given a list of matrices of type I, compute a Hadamard Matrix */
  private static Matrix<Integer> hadamardMatrixFromTypeI(List<Polynomial<Integer>> A, int n) {

    Ring<Matrix<Integer>> matrixRing = new MatrixRing<>(Integers.getInstance(), n);
    IntegerRingEmbedding<Matrix<Integer>> embedding = new IntegerRingEmbedding<>(
        matrixRing);

    // U could be a permutation matrix of order n
    Matrix<Integer> U = Matrix.of(n, n, (i, j) -> i == Math.floorMod(j - 1, n) ? 1 : 0);

    // Consider the A's as polynomials over matrices and apply them on U
    List<Matrix<Integer>> m = A.stream().map(p -> p.mapCoefficients(embedding::apply))
        .map(Ai -> Ai.apply(U, matrixRing)).collect(Collectors.toList());

    // Build Hadamard matrix as block matrix.
    return Matrix.fromBlocks(Matrix.of(4,
        m.get(0), m.get(1), m.get(2), m.get(3),
        matrixRing.negate(m.get(1)), m.get(0), matrixRing.negate(m.get(3)), m.get(2),
        matrixRing.negate(m.get(2)), m.get(3), m.get(0), matrixRing.negate(m.get(1)),
        matrixRing.negate(m.get(3)), matrixRing.negate(m.get(2)), m.get(1), m.get(0)));
  }

  private static List<Polynomial<Integer>> typeIIItoTypeI(List<Polynomial<Integer>> B, int n) {
    Ring<Polynomial<Integer>> ring = new FormalRing<>(n, Integers.getInstance());
    Sum<Polynomial<Integer>> sum = new Sum<>(ring);

    return IntStream.range(0, 4).mapToObj(i -> ring.subtract(sum.apply(B)
        .mapCoefficients(c -> c / 2), B.get(i))).collect(Collectors.toList());
  }

  /**
   * For an positive odd <i>k</i>, compute the unique <i>m</i> such that <i>k<sup>2</sup> = (1 +
   * 4m)<sup>2</sup></i>.
   */
  private static int computeM(int k) {
    if (k <= 0) {
      throw new IllegalArgumentException("k must be positive");
    }

    if (Math.floorMod(k, 4) == 1) {
      return (k - 1) / 4;
    } else if (Math.floorMod(k, 4) == 3) {
      return -((k + 1) / 4);
    } else {
      throw new IllegalArgumentException("k must be odd");
    }
  }

  // A polynomial ring with coefficients of type E where <i>x<sup>n</sup> = 1</i>.
  private static class FormalRing<E> extends PolynomialRingOverRing<E> {

    private final int n;

    public FormalRing(int n, Ring<E> ring) {
      super(ring);
      this.n = n;
    }

    @Override
    public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
      Polynomial.Builder<E> result = new Builder<>(super.getRing());
      a.forEachInParallel(
          (i, ai) -> b.forEachInParallel((j, bj) -> result.addTo(Math.floorMod(i + j, n),
              super.getRing().multiply(ai, bj))));
      return result.build();
    }

  }

}
