package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.structures.QuotientRing;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.math.BigInteger;

public class BigFiniteField extends QuotientRing<Polynomial<BigInteger>>
        implements Field<Polynomial<BigInteger>> {

    private final String stringRepresentation;
    private final BigInteger p;
    private final BigPrimeField baseField;

    /**
     * Create a finite field as a field of prime order module an irreducible polynomial.
     */
    public BigFiniteField(BigPrimeField baseField, Polynomial<BigInteger> mod) {
        super(new PolynomialRing<>(baseField), mod);

        this.p = baseField.getModulus();
        this.stringRepresentation = String.format("GF(%s^{%s})", baseField.getModulus(),
                mod.degree());
        this.baseField = baseField;
    }

    @Override
    public Polynomial<BigInteger> invert(Polynomial<BigInteger> a) {
        return new EuclideanAlgorithm<>((PolynomialRing<BigInteger>) super.ring)
                .applyExtended(a, mod).x();
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

    public BigInteger getPrime() {
        return p;
    }

    public int getExponent() {
        return this.mod.degree();
    }

}
