package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.MatrixInversion;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

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
        Matrix<E> vandermondeMatrix = Matrix.of(n, n, (i, j) -> power.apply(points.get(i).first, j));
        Matrix<E> inverse = new MatrixInversion<>(field).apply(vandermondeMatrix);
        Vector<E> a = inverse.apply(Vector.of(n, i -> points.get(i).second), field);

        Polynomial.Builder<E> builder = new Polynomial.Builder<>(field);
        for (int i = 0; i < a.size(); i++) {
            builder.set(i, a.get(i));
        }
        return builder.build();
    }
}
