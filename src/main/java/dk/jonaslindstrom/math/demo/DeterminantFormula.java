package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.algorithms.Determinant;
import dk.jonaslindstrom.math.algebra.concretisations.MultivariatePolynomialRingOverRing;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigIntegers;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import java.math.BigInteger;

<<<<<<< HEAD
/** Compute a closed formula for the determinant of a 6x6 matrix */
public class DeterminantFormula {

  public static void main(String[] arguments) {

    int n = 6;

=======
/** Compute a closed formula for the determinant of a 4x4 matrix */
public class DeterminantFormula {

  public static void main(String[] arguments) {
    int n = 4;
>>>>>>> ac6a691d5d7b9d636e315c4c416123e7ab2bfbdd
    MultivariatePolynomialRingOverRing<BigInteger> ring = new MultivariatePolynomialRingOverRing<>(
        BigIntegers.getInstance(), n * n);

    Matrix<MultivariatePolynomial<BigInteger>> matrix = Matrix.of(n, n, (i,j) -> {
      int[] degree = new int[n*n];
      degree[i * n + j] = 1;
      return MultivariatePolynomial.monomial(BigInteger.ONE, degree);
    });

    System.out.println(matrix);

    MultivariatePolynomial<BigInteger> determinant = new Determinant<>(ring).apply(matrix);
    System.out.println(determinant);
  }

}
