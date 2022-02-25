package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.elements.Fraction;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;

public class QuadraticField extends AlgebraicFieldExtension<Fraction<Integer>> {

  public QuadraticField(Integer d) {
    super(Rationals.getInstance(), "\\sqrt{" + d + "}", Polynomial.of(
        Rationals.getInstance(),
        new Fraction<>(-d, 1),
        new Fraction<>(0, 0),
        new Fraction<>(1, 1)));
  }
}
