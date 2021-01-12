package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.InnerProductSpace;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QRDecomposition<E, F extends Field<E>> implements Function<Matrix<E>, Pair<Matrix<E>, Matrix<E>>> {

  private final InnerProductSpace<Vector<E>, E, F> V;
  private final UnaryOperator<Vector<E>> normalize;

  public QRDecomposition(InnerProductSpace<Vector<E>, E, F> vectorSpace, UnaryOperator<Vector<E>> normalize) {
    this.V = vectorSpace;
    this.normalize = normalize;
  }

  @Override
  public Pair<Matrix<E>, Matrix<E>> apply(Matrix<E> A) {
    List<Vector<E>> columns = IntStream.range(0, A.getWidth()).mapToObj(A::getColumn).collect(
        Collectors.toList());

    GramSchmidt<Vector<E>, E, F> gramSchmidt = new GramSchmidt<>(V);

    List<Vector<E>> u = gramSchmidt.apply(columns);
    List<Vector<E>> e = u.parallelStream().map(normalize).collect(Collectors.toList());

    Matrix<E> Q = Matrix.of(A.getHeight(), A.getHeight(), (i,j) -> e.get(j).get(i));
    Matrix<E> R = Matrix.of(A.getHeight(), A.getWidth(), (i,j) ->
      i <= j ? V.innerProduct(e.get(i), A.getColumn(j)) : V.getScalars().getZero()
    );

    return new Pair<>(Q, R);
  }

}