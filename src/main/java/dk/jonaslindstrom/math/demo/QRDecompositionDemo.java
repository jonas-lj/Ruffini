package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.algorithms.QRDecomposition;
import dk.jonaslindstrom.math.algebra.concretisations.RealCoordinateSpace;
import dk.jonaslindstrom.math.algebra.concretisations.RealNumbers;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.util.Pair;

public class QRDecompositionDemo {

  public static void main(String[] arguments) {
    Matrix<Integer> A = Matrix.of(3,
        12, -51, 4,
        6, 167, -68,
        -4, 24, -41);

    Matrix<Double> B = A.forEach(Double::valueOf);

    RealCoordinateSpace V = new RealCoordinateSpace(3);
    QRDecomposition<Double, RealNumbers> qrDecomposition = new QRDecomposition<>(
        V,
        v -> V.scale(1.0 / V.norm(v), v));

    Pair<Matrix<Double>, Matrix<Double>> result = qrDecomposition.apply(B);

    System.out.println(result.first);
    System.out.println(result.second);
  }

}
