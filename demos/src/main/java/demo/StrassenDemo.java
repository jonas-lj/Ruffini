package demo;

import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingRing;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.MatrixMultiplication;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.StrassenMultiplication;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import org.junit.Assert;

import java.util.Random;

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
