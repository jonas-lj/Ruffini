package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.elements.Fraction;
import dk.jonaslindstrom.ruffini.integers.structures.Rationals;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

public class GaussianRationals extends AlgebraicFieldExtension<Fraction<Integer>, Rationals> {

    public GaussianRationals() {
        super(Rationals.getInstance(), "i", Polynomial.of(
                Rationals.getInstance(),
                new Fraction<>(1, 1),
                new Fraction<>(0, 0),
                new Fraction<>(1, 1)));
    }
}
