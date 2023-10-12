package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.DiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.algorithms.InverseDiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.SortedMap;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class PolynomialRingFFT<E> extends PolynomialRing<E> {

    private final SortedMap<Integer, E> rootsOfUnity;

    public PolynomialRingFFT(Field<E> field, SortedMap<Integer, E> rootsOfUnity) {
        super(field);
        this.rootsOfUnity = rootsOfUnity;
    }

    @Override
    public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
        int n = a.degree() + b.degree() + 1;
        if (rootsOfUnity.containsKey(n)) {
            DiscreteFourierTransform<E> DFT = new DiscreteFourierTransform<>(field, rootsOfUnity.get(n), n);
            Vector<E> â = DFT.apply(a.vector(this.field.zero()));
            Vector<E> b̂ = DFT.apply(b.vector(this.field.zero()));
            Vector<E> ĉ = â.coordinateWise(b̂, this.field::multiply);
            InverseDiscreteFourierTransform<E> inverseDFT = new InverseDiscreteFourierTransform<>(field, rootsOfUnity.get(n), n);
            return new Polynomial<>(inverseDFT.apply(ĉ), this.field);
        } else {
            return super.multiply(a, b);
        }
    }
}
