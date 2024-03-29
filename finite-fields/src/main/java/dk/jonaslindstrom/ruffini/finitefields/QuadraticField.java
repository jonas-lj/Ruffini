package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.elements.Fraction;
import dk.jonaslindstrom.ruffini.integers.structures.Rationals;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

public class QuadraticField extends AlgebraicFieldExtension<Fraction<Integer>, Rationals> {

    public QuadraticField(Integer d) {
        super(Rationals.getInstance(), "\\sqrt{" + d + "}", Polynomial.of(
                new Fraction<>(-d, 1),
                new Fraction<>(0, 0),
                new Fraction<>(1, 1)));
    }
}
