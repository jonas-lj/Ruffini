package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.abstractions.VectorSpace;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.algorithms.GramSchmidt;
<<<<<<< HEAD
import dk.jonaslindstrom.math.algebra.algorithms.Power;
=======
>>>>>>> ac6a691d5d7b9d636e315c4c416123e7ab2bfbdd
import dk.jonaslindstrom.math.algebra.algorithms.QuadraticEquation;
import dk.jonaslindstrom.math.algebra.concretisations.ConstructiveReals;
import dk.jonaslindstrom.math.algebra.concretisations.MatrixRing;
import dk.jonaslindstrom.math.algebra.concretisations.VectorSpaceOverField;
import dk.jonaslindstrom.math.algebra.elements.ConstructiveReal;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.math.BigDecimal;
<<<<<<< HEAD
import java.math.RoundingMode;
import java.time.Instant;
=======
>>>>>>> ac6a691d5d7b9d636e315c4c416123e7ab2bfbdd
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

<<<<<<< HEAD
/**
 * A demo of using constructive reals for various computations.
 */
=======
>>>>>>> ac6a691d5d7b9d636e315c4c416123e7ab2bfbdd
public class ConstructiveRealsDemo {

  public static void main(String[] arguments) {

    System.out.println("Demo of constructive reals");

    // Number of bits of precision
    int m = 512;

    ConstructiveReals ℝ = new ConstructiveReals();
    DotProduct<ConstructiveReal> dotProduct = new DotProduct<>(ℝ);

    // Estimate the square root of 2
    BigDecimal sqrt2 = new BigDecimal("1.414213562373095048801688724209698078569671875376948073176679737990732478462107038850387534327641572735013846230912297024924836055850737212644121497099935831413");
    System.out.println();
    System.out.println("Computing square root of 2...");
    BigDecimal sqrt2estimate = ConstructiveReal.sqrt(ConstructiveReal.of(2)).estimate(m);
    System.out.println("Actual  : " + sqrt2estimate);
    System.out.println("Expected: " + sqrt2);
    System.out.println("Error   : " + sqrt2.subtract(sqrt2estimate).abs());

    // Compute the golden ratio as root of x^2 - x - 1
    BigDecimal phi = new BigDecimal("1.6180339887498948482045868343656381177203091798057628621354486227052604628189024497072072041893911374847540880753868917521266338622235369317931800607667263544333");
    System.out.println();
    System.out.println("Finding root of x^2 - x - 1...");
    QuadraticEquation<ConstructiveReal, ConstructiveReals> quadraticEquation = QuadraticEquation.create(
        ConstructiveReal.of(1), ConstructiveReal.of(-1), ConstructiveReal.of(-1));
    BigDecimal z = quadraticEquation.solve().estimate(m);
    System.out.println("Actual  : " + z);
    System.out.println("Expected: " + phi);
    System.out.println("Error   : " + z.subtract(phi).abs());

    // Apply Gram-Schmidt process to nxn random matrix
    Random random = new Random(1234);
    int n = 4;
    VectorSpace<Vector<ConstructiveReal>, ConstructiveReal, ConstructiveReals> V = new VectorSpaceOverField<>(ℝ, n);

    System.out.println();
    System.out.println("Applying Gram-Schmidt process to random matrix...");
    Matrix<Double> mPrime = Matrix.of(n, n, (i,j) -> random.nextDouble(), true);
    Matrix<ConstructiveReal> matrix = mPrime.map(ConstructiveReal::of);
    GramSchmidt<Vector<ConstructiveReal>, ConstructiveReal, ConstructiveReals> gramSchmidt =
        new GramSchmidt<>(V, dotProduct);
    List<Vector<ConstructiveReal>> vectors = gramSchmidt.apply(Vector.of(n, matrix::getColumn).asList());

    List<ConstructiveReal> magnitudes = vectors.stream().map(c -> dotProduct.apply(c,c)).map(ConstructiveReal::sqrt).collect(
        Collectors.toList());
    Matrix<ConstructiveReal> result = Matrix.of(n, n, (i, j) -> ℝ.divide(vectors.get(i).get(j), magnitudes.get(i)));
    System.out.println(result.map(xij -> xij.estimate(m)));

    System.out.println();
    System.out.println("Computing I - A^T A. Should be the zero matrix...");
    MatrixRing<ConstructiveReal> ring = new MatrixRing<>(ℝ, n);
    Matrix<BigDecimal> innerProducts = ring.subtract(ring.getIdentity(), ring.multiply(result, result.transpose())).map(xij -> xij.estimate(m));
    System.out.println(innerProducts);

<<<<<<< HEAD

    // Compute 1000 digits of pi
    System.out.println();
    System.out.println("Computing 1000 digits of pi...");
    int digits = 1000;
    int M = (int) Math.ceil((digits + 1) * 3.322);

    Power<ConstructiveReal> power = new Power<>(ℝ);
    ConstructiveReal sum = ConstructiveReal.of(0);

    ConstructiveReal inv16 = ConstructiveReal.reciprocal(ConstructiveReal.of(16));
    ConstructiveReal four = ConstructiveReal.of(4);
    ConstructiveReal two = ConstructiveReal.of(2);

    for (int k = 0; k < M / 4; k++) {
      ConstructiveReal a = power.apply(inv16, k);
      ConstructiveReal b = ℝ.divide(four, ConstructiveReal.of(8*k+1));
      ConstructiveReal c = ℝ.divide(two, ConstructiveReal.of(8*k+4));
      ConstructiveReal d = ConstructiveReal.reciprocal(ConstructiveReal.of(8*k+5));
      ConstructiveReal e = ConstructiveReal.reciprocal(ConstructiveReal.of(8*k+6));
      sum = ℝ.add(sum, ℝ.multiply(a, ℝ.subtract(b, ℝ.add(c,d,e))));
    }
    System.out.println(sum.estimate(M).setScale(digits, RoundingMode.HALF_UP));

=======
>>>>>>> ac6a691d5d7b9d636e315c4c416123e7ab2bfbdd
  }

}
