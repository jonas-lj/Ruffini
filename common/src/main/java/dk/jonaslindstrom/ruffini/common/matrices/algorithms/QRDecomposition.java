package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.InnerProductSpace;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Compute the QR decomposition of a matrix.
 *
 * @param <E> Element type.
 * @param <F> Field type.
 */
public class QRDecomposition<E, F extends Field<E>> implements
        Function<Matrix<E>, Pair<Matrix<E>, Matrix<E>>> {

    private final InnerProductSpace<Vector<E>, E, F> V;
    private final UnaryOperator<Vector<E>> normalize;

    public QRDecomposition(InnerProductSpace<Vector<E>, E, F> vectorSpace,
                           UnaryOperator<Vector<E>> normalize) {
        this.V = vectorSpace;
        this.normalize = normalize;
    }

    @Override
    public Pair<Matrix<E>, Matrix<E>> apply(Matrix<E> A) {
        List<Vector<E>> columns = IntStream.range(0, A.getWidth()).mapToObj(A::getColumn).collect(
                Collectors.toList());

        GramSchmidt<Vector<E>, E, F> gramSchmidt = new GramSchmidt<>(V);

        List<Vector<E>> u = gramSchmidt.apply(columns);
        List<Vector<E>> e = u.parallelStream().map(normalize).toList();

        Matrix<E> Q = Matrix.of(A.getHeight(), A.getHeight(), (i, j) -> e.get(j).get(i));
        Matrix<E> R = Matrix.of(A.getHeight(), A.getWidth(), (i, j) ->
                i <= j ? V.innerProduct(e.get(i), A.getColumn(j)) : V.getScalars().zero()
        );

        return new Pair<>(Q, R);
    }

}