package dk.jonaslindstrom.ruffini.polynomials.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.DiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.algorithms.InverseDiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the ring of polynomials <i>K[x]</i> over a field <i>K</i>.
 */
public class PolynomialRingFFT<E> implements Ring<PolynomialRingFFT<E>.TransformedPolynomial> {

    private final int n;
    private final DiscreteFourierTransform<E> fft;
    private final InverseDiscreteFourierTransform<E> ifft;
    private final Field<E> field;
    private final E root;

    public PolynomialRingFFT(Field<E> field, E rootOfUnity, int n) {
        this.field = field;
        this.n = n;
        this.root = rootOfUnity;
        this.fft = new DiscreteFourierTransform<>(field, rootOfUnity, n);
        this.ifft = new InverseDiscreteFourierTransform<>(field, rootOfUnity, n);
    }

    public Field<E> getField() {
        return field;
    }

    /** Transformation of x^m */
    public TransformedPolynomial monomial(int m) {
        E am = field.power(root, m);
        List<E> coefficients = new ArrayList<>();
        coefficients.add(field.identity());
        coefficients.add(am);
        E current = am;
        for (int i = 2; i < n; i++) {
            current = field.multiply(current, am);
            coefficients.add(current);
        }
        return new TransformedPolynomial(Vector.ofList(coefficients));
    }

    public TransformedPolynomial fromConstant(E a) {
        return new TransformedPolynomial(Vector.of(n, i -> a));
    }

    public TransformedPolynomial scale(TransformedPolynomial a, E b) {
        return new TransformedPolynomial(a.coefficients.map(c -> field.multiply(c, b)));
    }

    public TransformedPolynomial fromPolynomial(Polynomial<E> a) {
        return new TransformedPolynomial(a);
    }

    public TransformedPolynomial fromFourierCoefficients(Vector<E> a) {
        return new TransformedPolynomial(a);
    }

    public Polynomial<E> toPolynomial(TransformedPolynomial a) {
        return a.toPolynomial();
    }

    @Override
    public TransformedPolynomial negate(TransformedPolynomial a) {
        return new TransformedPolynomial(a.coefficients.map(field::negate));
    }

    @Override
    public TransformedPolynomial add(TransformedPolynomial a, TransformedPolynomial b) {
        return new TransformedPolynomial(a.coefficients.coordinateWise(b.coefficients, field::add));
    }

    @Override
    public TransformedPolynomial zero() {
        return new TransformedPolynomial(Vector.of(n, i -> field.zero()));
    }

    @Override
    public TransformedPolynomial identity() {
        return new TransformedPolynomial(Vector.of(n, i -> field.identity()));
    }

    @Override
    public TransformedPolynomial multiply(TransformedPolynomial a, TransformedPolynomial b) {
        return new TransformedPolynomial(a.coefficients.coordinateWise(b.coefficients, field::multiply));
    }

    @Override
    public String toString(TransformedPolynomial a) {
        return a.toString();
    }

    @Override
    public boolean equals(TransformedPolynomial a, TransformedPolynomial b) {
        return a.coefficients.equals(b.coefficients);
    }


    public class TransformedPolynomial {

        private final Vector<E> coefficients;

        public TransformedPolynomial(Polynomial<E> from) {
            this(fft.apply(from.vector(field.zero())));
        }

        private TransformedPolynomial(Vector<E> coefficients) {
            this.coefficients = coefficients;
        }

        public Vector<E> getCoefficients() {
            return coefficients;
        }

        public Polynomial<E> toPolynomial() {
            return new Polynomial<>(ifft.apply(this.coefficients), field);
        }

        public String toString() {
            return "TransformedPolynomial(" + coefficients + ")";
        }

    }

}
