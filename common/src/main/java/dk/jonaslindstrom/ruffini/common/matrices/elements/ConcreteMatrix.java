package dk.jonaslindstrom.ruffini.common.matrices.elements;

import dk.jonaslindstrom.ruffini.common.functional.IntBinaryFunction;
import dk.jonaslindstrom.ruffini.common.vector.ConcreteVector;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A concrete instantiation of a {@link Matrix} using {@link ArrayList}s.
 *
 * @param <E> Element type.
 */
class ConcreteMatrix<E> extends BaseMatrix<E> {

    protected final ArrayList<ArrayList<E>> rows;

    ConcreteMatrix(int m, int n, IntBinaryFunction<E> populator) {
        this(m, n, populator, true);
    }

    ConcreteMatrix(int m, int n, IntBinaryFunction<E> populator, boolean sequential) {
        this(m, i -> (sequential ? IntStream.range(0, n) : IntStream.range(0, n).parallel())
                .mapToObj(j -> populator.apply(i, j)).collect(Collectors.toCollection(ArrayList::new)), sequential);
    }

    ConcreteMatrix(int m, IntFunction<ArrayList<E>> rowPopulator) {
        this(m, rowPopulator, true);
    }

    ConcreteMatrix(int m, IntFunction<ArrayList<E>> rowPopulator, boolean sequential) {
        this((sequential ? IntStream.range(0, m) : IntStream.range(0, m).parallel())
                .mapToObj(rowPopulator).collect(Collectors.toCollection(ArrayList::new)));
    }

    ConcreteMatrix(ArrayList<ArrayList<E>> rows) {
        this.rows = rows;
    }

    /**
     * Return a submatrix of this matrix containing row <i>r0,...,r1-1</i> and column
     * <i>c0,...,c1-1</i>.
     */
    @Override
    public Matrix<E> submatrix(int r0, int r1, int c0, int c1) {
        assert (r0 < r1 && c0 < c1);
        return submatrix(IntStream.range(r0, r1).toArray(), IntStream.range(c0, c1).toArray());
    }

    @Override
    public Matrix<E> minor(int i, int j) {
        return new ConcreteMatrix<>(getHeight() - 1, getWidth() - 1,
                (a, b) -> get(a >= i ? a + 1 : a, b >= j ? b + 1 : b));
    }

    @Override
    public Matrix<E> transpose() {
        return new ConcreteMatrix<>(getWidth(), getHeight(), (i, j) -> get(j, i));
    }

    /**
     * Return a submatrix of this matrix with the given rows and columns.
     *
     * @param rows    Rows to include in submatrix.
     * @param columns Columns to include in submatrix.
     */
    @Override
    public Matrix<E> submatrix(int[] rows, int[] columns) {
        return new ConcreteMatrix<>(rows.length, columns.length, (i, j) -> get(rows[i], columns[j]));
    }

    @Override
    public E get(int i, int j) {
        return rows.get(i).get(j);
    }

    void set(int i, int j, E e) {
        rows.get(i).set(j, e);
    }

    @Override
    public ConcreteVector<E> getRow(int i) {
        return new ConcreteVector<>(rows.get(i));
    }

    @Override
    public ConcreteVector<E> getColumn(int j) {
        return new ConcreteVector<>(getHeight(), i -> getRow(i).get(j));
    }

    @Override
    public int getHeight() {
        return rows.size();
    }

    @Override
    public int getWidth() {
        return rows.get(0).size();
    }

    @Override
    public Iterable<Vector<E>> rows() {
        return () -> rows.stream().map(Vector::ofList).iterator();
    }

    @Override
    public Matrix<E> view() {
        return this;
    }

    @Override
    public Matrix<E> extendTo(int m, int n, E padding) {
        assert (m >= getHeight() && n >= getWidth());
        return new ConcreteMatrix<>(m, n, (i, j) -> {
            if (i < getHeight() && j < getWidth()) {
                return get(i, j);
            } else {
                return padding;
            }
        });
    }

    @Override
    public <F> Matrix<F> map(Function<E, F> f) {
        return Matrix.of(getHeight(), getWidth(), (i, j) -> f.apply(get(i, j)));
    }

}

