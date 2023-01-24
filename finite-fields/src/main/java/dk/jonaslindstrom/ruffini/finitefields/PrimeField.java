package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.exceptions.NotInvertibleException;
import dk.jonaslindstrom.ruffini.common.util.Triple;
import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import dk.jonaslindstrom.ruffini.integers.structures.IntegersModuloN;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

public class PrimeField extends IntegersModuloN implements Field<Integer> {

    public PrimeField(Integer p) {
        super(p);
    }

    @Override
    public Integer invert(Integer a) {
        Triple<Integer, Integer, Integer> gcd = new EuclideanAlgorithm<>(Integers.getInstance())
                .extendedGcd(a, super.mod);
        if (gcd.getFirst() != 1) {
            throw new NotInvertibleException(super.mod);
        }
        Integer m = gcd.getSecond();
        return Math.floorMod(m, super.mod);
    }

    @Override
    public String toString() {
        return "\\mathbb{F}_{" + super.mod.toString() + "}";
    }

    public FiniteField asFiniteField() {
        return new FiniteField(this, Polynomial.monomial(1, 1));
    }

}
