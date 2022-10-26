package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.matrices.algorithms.Determinant;
import dk.jonaslindstrom.ruffini.common.matrices.elements.Matrix;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.Objects;
import java.util.function.Function;

public class CharacteristicPolynomial<E> implements Function<Matrix<E>, Polynomial<E>> {

    private final Ring<E> ring;
    private final PolynomialRingOverRing<E> polynomialRing;

    public CharacteristicPolynomial(Ring<E> ring) {
        this.ring = ring;
        this.polynomialRing = new PolynomialRingOverRing<>(ring);
    }

    @Override
    public Polynomial<E> apply(Matrix<E> a) {
        assert (a.getHeight() == a.getWidth());

        Matrix<Polynomial<E>> d = Matrix.of(a.getHeight(), a.getHeight(),
                (i, j) -> (Objects.equals(i, j) ? Polynomial
                        .of(ring, ring.negate(a.get(i, j)), ring.getIdentity())
                        : Polynomial.constant(ring.negate(a.get(i, j)))));

        return new Determinant<>(polynomialRing).apply(d);
    }

}
