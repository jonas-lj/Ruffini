package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;

import java.util.function.Function;

public class Determinant<E> implements Function<Matrix<E>, E> {

    private final Ring<E> ring;

    public Determinant(Ring<E> ring) {
        this.ring = ring;
    }

    @Override
    public E apply(Matrix<E> t) {
        assert (t.isSquare());

        if (t.getHeight() == 1) {
            return t.get(0, 0);
        } else {
            E d = ring.zero();
            E c = ring.identity();
            Matrix<E> view = t.view();
            for (int i = 0; i < t.getHeight(); i++) {
                d = ring.add(d, ring.multiply(t.get(i, 0), ring.multiply(c, apply(view.minor(i, 0)))));
                c = ring.negate(c);
            }
            return d;
        }
    }

}
