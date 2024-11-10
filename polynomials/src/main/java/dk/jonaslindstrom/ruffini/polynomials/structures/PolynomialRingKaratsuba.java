package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.FastDivision;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.KaratsubaAlgorithm;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class PolynomialRingKaratsuba<E> extends PolynomialRing<E> {

    private final KaratsubaAlgorithm<E> karatsuba;

    public PolynomialRingKaratsuba(Field<E> field) {
        super(field);
        this.karatsuba = new KaratsubaAlgorithm<>(this);
    }

    @Override
    public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
        int n = Math.max(a.degree(), b.degree()) + 1;
        if (n <= 2) {
            return super.multiply(a, b);
        }
        return karatsuba.apply(a, b);
    }

    @Override
    public Pair<Polynomial<E>, Polynomial<E>> divide(Polynomial<E> a, Polynomial<E> b) {
        return new FastDivision<>(this).apply(a, b);
    }
}
