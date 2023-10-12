package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.math.BigInteger;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class PolynomialRing<E> extends PolynomialRingOverRing<E>
        implements EuclideanDomain<Polynomial<E>> {

    protected final Field<E> field;

    public PolynomialRing(Field<E> field) {
        super(field);
        this.field = field;
    }

    public Field<E> getBaseField() {
        return field;
    }

    @Override
    public Pair<Polynomial<E>, Polynomial<E>> divide(Polynomial<E> a,
                                                     Polynomial<E> b) {
        return super.divisionWithRemainder(a, b, field.invert(b.getCoefficient(b.degree())));
    }

    @Override
    public BigInteger norm(Polynomial<E> a) {
        return BigInteger.valueOf(a.degree());
    }

    @Override
    public String toString() {
        return field.toString() + "(x)";
    }

}
