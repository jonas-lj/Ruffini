package dk.jonaslindstrom.ruffini.elliptic.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.elements.Fraction;
import dk.jonaslindstrom.ruffini.common.functional.TriFunction;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.ShortWeierstrassCurveAffine;
import dk.jonaslindstrom.ruffini.polynomials.elements.MultivariatePolynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.MultivariatePolynomialRing;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Given two points P and Q of order m on an elliptic curve, this algorithm computes f(Q) where
 * div(f) = [m]P - m[O]. It is used in {@link WeilPairing}.
 */
public class MillersAlgorithm<E> implements
        TriFunction<AffinePoint<E>, AffinePoint<E>, BigInteger, E> {

    private final ShortWeierstrassCurveAffine<E> curve;
    private final Field<E> field;
    private final MultivariatePolynomialRing<E> polynomialRing;

    public MillersAlgorithm(ShortWeierstrassCurveAffine<E> curve) {
        this.curve = curve;
        this.field = curve.getField();
        this.polynomialRing = new MultivariatePolynomialRing<>(field, 2);
    }

    private static List<Boolean> toBits(BigInteger m) {
        return IntStream.range(0, m.bitLength()).mapToObj(m::testBit).collect(Collectors.toList());
    }

    @Override
    public E apply(AffinePoint<E> P, AffinePoint<E> Q, BigInteger m) {

        List<Boolean> mBits = toBits(m);

        AffinePoint<E> T = P;
        E f = field.getIdentity();

        // Miller's algorithm in its general form
        for (int i = mBits.size() - 2; i >= 0; i--) {
            f = field.multiply(f, f, evaluate(g(T, T), Q));
            T = curve.add(T, T);
            if (mBits.get(i)) {
                f = field.multiply(f, evaluate(g(T, P), Q));
                T = curve.add(T, P);
            }
        }

        return f;
    }

    private Fraction<MultivariatePolynomial<E>> g(AffinePoint<E> p, AffinePoint<E> q) {

        if (field.equals(p.x(), q.x()) && !curve.equals(p, q)) {

            // Vertical slope
            MultivariatePolynomial.Builder<E> builder = new MultivariatePolynomial.Builder<>(2, field);
            builder.add(field.getIdentity(), 1, 0);
            builder.add(field.negate(p.x()), 0, 0);
            return new Fraction<>(builder.build(), polynomialRing.getIdentity());

        } else {

            E λ;
            if (curve.equals(p, q)) {
                // Compute tangent
                E xSquare = field.multiply(p.x(), p.x());
                λ = field.divide(
                        field.add(xSquare, xSquare, xSquare, curve.getA()),
                        field.add(p.y(), p.y()));
            } else {
                // Compute slope
                λ = field.divide(
                        field.subtract(q.y(), p.y()),
                        field.subtract(q.x(), p.x()));
            }

            // y - λ x - λ px - py
            MultivariatePolynomial.Builder<E> builder = new MultivariatePolynomial.Builder<>(2, field);
            builder.add(field.getIdentity(), 0, 1);
            builder.add(field.negate(λ), 1, 0);
            builder.add(field.subtract(field.multiply(λ, p.x()), p.y()), 0, 0);
            MultivariatePolynomial<E> numerator = builder.build();

            // x + px + qx - λ^2
            builder = new MultivariatePolynomial.Builder<>(2, field);
            builder.add(field.getIdentity(), 1, 0);
            builder.add(field.subtract(field.add(p.x(), q.x()), field.multiply(λ, λ)), 0, 0);
            MultivariatePolynomial<E> denominator = builder.build();

            return new Fraction<>(numerator, denominator);
        }
    }

    private E evaluate(Fraction<MultivariatePolynomial<E>> polynomial, AffinePoint<E> point) {
        Vector<E> v = Vector.of(point.x(), point.y());
        return field.divide(polynomial.numerator().apply(v, field),
                polynomial.denominator().apply(v, field));
    }

}
