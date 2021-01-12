package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.elements.Fraction;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;

public class GaussianRationals extends AlgebraicFieldExtension<Fraction<Integer>> {
    public GaussianRationals() {
        super(Rationals.getInstance(), "i", Polynomial.of(
            Rationals.getInstance(),
            new Fraction<>(1, 1),
            new Fraction<>(0, 0),
            new Fraction<>(1, 1)));
    }
}
