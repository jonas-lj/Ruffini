package dk.jonaslindstrom.ruffini.common.matrices.elements;


import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.DotProduct;
import dk.jonaslindstrom.ruffini.common.vector.ConcreteVector;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * This class represents a matrix of elements of type E. Operations that can be done without knowing how the entries are
 * represented should be implemented here.
 */
public abstract class BaseMatrix<E> implements Matrix<E> {

    @Override
    public boolean isSquare() {
        return getHeight() == getWidth();
    }

    @Override
    public Vector<E> apply(Vector<E> b, Ring<E> ring) {
        if (b.size() != getWidth()) {
            throw new IllegalArgumentException();
        }

        DotProduct<E> innerProduct = new DotProduct<>(ring);

        return new ConcreteVector<>(getHeight(), i -> innerProduct.apply(getRow(i), b));
    }


    @Override
    public String toString() {
        return toString(Objects::toString);
    }

    public String toString(Function<E, String> toString) {
        // Assume LaTeX asmmath package is loaded
        StringBuilder sb = new StringBuilder();
        sb.append("\\begin{pmatrix}\n");
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                sb.append(toString.apply(get(i, j)));
                sb.append(" ");
                if (j < getWidth() - 1) {
                    sb.append("& ");
                }
            }
            if (i < getHeight() - 1) {
                sb.append("\\\\");
            }
            sb.append("\n");
        }
        sb.append("\\end{pmatrix}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Matrix<E> other, BiPredicate<E, E> equality) {
        if (getHeight() != other.getHeight() || getWidth() != other.getWidth()) {
            return false;
        }

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (!equality.test(this.get(i, j), other.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Matrix<E> other = (Matrix<E>) obj;

        return equals(other, (x, y) -> x == y);
    }

    @Override
    public Iterable<Vector<E>> rows() {
        return () -> IntStream.range(0, getHeight()).mapToObj(this::getRow).iterator();
    }

    @Override
    public Iterable<Vector<E>> columns() {
        return () -> IntStream.range(0, getWidth()).mapToObj(this::getColumn).iterator();
    }

    @Override
    public Vector<E> getRow(int i) {
        return new ConcreteVector<>(getWidth(), j -> get(i, j));
    }

    @Override
    public Vector<E> getColumn(int j) {
        return new ConcreteVector<>(getHeight(), i -> get(i, j));
    }

    @Override
    public MutableMatrix<E> mutable() {
        return new MutableMatrix<>(this);
    }
}
