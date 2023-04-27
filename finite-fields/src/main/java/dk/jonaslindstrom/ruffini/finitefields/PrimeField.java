package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.exceptions.NotInvertibleException;
import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import dk.jonaslindstrom.ruffini.integers.structures.IntegersModuloN;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

public class PrimeField extends IntegersModuloN implements Field<Integer> {

    public PrimeField(Integer p) {
        super(p);
    }

    @Override
    public Integer invert(Integer a) {
        EuclideanAlgorithm.Result<Integer> gcd = new EuclideanAlgorithm<>(Integers.getInstance())
                .gcd(a, super.mod);
        if (gcd.gcd() != 1) {
            throw new NotInvertibleException(super.mod);
        }
        return Math.floorMod(gcd.x(), super.mod);
    }

    @Override
    public String toString() {
        return "\\mathbb{F}_{" + super.mod.toString() + "}";
    }

    public FiniteField asFiniteField() {
        return new FiniteField(this, Polynomial.monomial(1, 1));
    }

}
