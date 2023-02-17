package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.structures.QuotientRing;
import dk.jonaslindstrom.ruffini.common.util.Triple;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

public class AlgebraicFieldExtension<E, F extends Field<E>> extends QuotientRing<Polynomial<E>> implements
        Field<Polynomial<E>> {

    private final String element;

    private final F field;

    public AlgebraicFieldExtension(F field, String element, Polynomial<E> minimalPolynomial) {
        super(new PolynomialRing<>(field), minimalPolynomial);
        this.element = element;
        this.field = field;
    }

    @Override
    public Polynomial<E> invert(Polynomial<E> a) {
        Triple<Polynomial<E>, Polynomial<E>, Polynomial<E>> r =
                new EuclideanAlgorithm<>((PolynomialRing<E>) super.ring).extendedGcd(a, super.mod);
        assert (r.getFirst().degree() == 0);
        return r.getSecond().scale(field.invert(r.getFirst().getCoefficient(0)), field);
    }

    public F getBaseField() {
        return field;
    }

    public Polynomial<E> embed(E value) {
        return Polynomial.constant(value);
    }

    public String toString(Polynomial<E> p) {
        return p.toString(this.element);
    }

    public String toString() {
        return field.toString() + "(" + this.element + ")";
    }
}


