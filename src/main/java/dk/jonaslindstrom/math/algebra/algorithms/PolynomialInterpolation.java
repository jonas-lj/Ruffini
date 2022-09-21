package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.elements.polynomial.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.polynomial.Polynomial.Builder;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import java.util.List;
import java.util.function.Function;

public class PolynomialInterpolation<E> implements Function<List<Pair<E, E>>, Polynomial<E>> {

  private final Field<E> field;

  public PolynomialInterpolation(Field<E> field) {
    this.field = field;
  }

  @Override
  public Polynomial<E> apply(List<Pair<E, E>> points) {
    int n = points.size();

    Power<E> power = new Power<>(field);
    Matrix<E> frobeniusMatrix = Matrix.of(n, n, (i,j) -> power.apply(points.get(i).first, j));
    Matrix<E> inverse = new MatrixInversion<>(field).apply(frobeniusMatrix);
    Vector<E> a = inverse.apply(Vector.of(n, i -> points.get(i).second), field);

    Polynomial.Builder<E> builder = new Builder<>(field);
    for (int i = 0; i < a.size(); i++) {
      builder.set(i, a.get(i));
    }
    return builder.build();
  }
}
