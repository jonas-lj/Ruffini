package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRingOverRing;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import java.util.function.Function;

public class CharacteristicPolynomial<E> implements Function<Matrix<E>, Polynomial<E>> {

  private Ring<E> ring;
  private PolynomialRingOverRing<E> polynomialRing;

  public CharacteristicPolynomial(Ring<E> ring) {
    this.ring = ring;
    this.polynomialRing = new PolynomialRingOverRing<>(ring);
  }

  @Override
  public Polynomial<E> apply(Matrix<E> a) {
    assert (a.getHeight() == a.getWidth());

    Matrix<Polynomial<E>> d = Matrix.of(a.getHeight(), a.getHeight(),
        (i, j) -> (i == j ? Polynomial.of(ring, ring.negate(a.get(i, j)), ring.getIdentity())
            : Polynomial.constant(ring.negate(a.get(i, j)))));

    Polynomial<E> determinant = new Determinant<>(polynomialRing).apply(d);

    return determinant;
  }

}
