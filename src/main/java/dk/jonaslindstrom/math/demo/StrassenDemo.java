package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.algorithms.MatrixMultiplication;
import dk.jonaslindstrom.math.algebra.algorithms.StrassenMultiplication;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.helpers.PerformanceLoggingRing;
import java.util.Random;
import org.junit.Assert;

/**
 * Compute the product of two 8x8 matrices using the traditional algorithm and Strassens algorithm
 * and compare the number of operations used for each.
 */
public class StrassenDemo {

  public static void main(String[] arguments) {

    Random random = new Random(1234);

    Matrix<Integer> a = Matrix.of(8, 8, (i, j) -> random.nextInt(10));
    Matrix<Integer> b = Matrix.of(8, 8, (i, j) -> random.nextInt(10));

    PerformanceLoggingRing<Integer> logger = new PerformanceLoggingRing<>(Integers.getInstance());

    MatrixMultiplication<Integer> multiplication = new MatrixMultiplication<Integer>(logger);
    Matrix<Integer> d = multiplication.apply(a, b);
    System.out.println("Naive multiplication");
    System.out.println("====================");
    System.out.println(logger);
    System.out.println();

    logger.reset();

    StrassenMultiplication<Integer> strassenMultiplication = new StrassenMultiplication<>(logger);
    Matrix<Integer> c = strassenMultiplication.apply(a, b);
    System.out.println("Strassen multiplication");
    System.out.println("=======================");
    System.out.println(logger);

    Assert.assertTrue(c.equals(d, logger::equals));
  }

}
