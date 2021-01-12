package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.util.Triple;

public class AlgebraicFieldExtension<E> extends QuotientRing<Polynomial<E>> implements Field<Polynomial<E>> {

    private final String element;
    private final Field<E> field;

    public AlgebraicFieldExtension(Field<E> field, String element, Polynomial<E> minimalPolynomial) {
        super(new PolynomialRing<>(field), minimalPolynomial);
        this.element = element;
        this.field = field;
    }

    @Override
    public Polynomial<E> invert(Polynomial<E> a) {
        Triple<Polynomial<E>, Polynomial<E>, Polynomial<E>> r = new EuclideanAlgorithm<>((PolynomialRing<E>) super.ring).extendedGcd(a, super.mod);
        assert(r.first.degree() == 0);
        return r.second.scale(field.invert(r.first.getCoefficient(0)), field);
    }

    public String toString(Polynomial<E> p) {
        return p.toString(this.element);
    }

    @Override
    public int getCharacteristics() {
        return field.getCharacteristics();
    }
}


