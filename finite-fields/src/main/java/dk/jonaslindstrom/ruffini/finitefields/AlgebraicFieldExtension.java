package dk.jonaslindstrom.ruffini.finitefields;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.structures.QuotientRing;
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
        EuclideanAlgorithm.Result<Polynomial<E>> r =
                new EuclideanAlgorithm<>((PolynomialRing<E>) super.ring).gcd(a, super.mod);
        assert (r.gcd().degree() == 0);
        return r.x().scale(field.invert(r.gcd().getCoefficient(0)), field);
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


