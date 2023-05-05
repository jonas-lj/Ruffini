package dk.jonaslindstrom.ruffini.common.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.elements.Fraction;

public class FieldOfFractions<E> implements Field<Fraction<E>> {

    private final EuclideanDomain<E> baseRing;
    private final EuclideanAlgorithm<E> euclideanAlgorithm;

    public FieldOfFractions(EuclideanDomain<E> ring) {
        this.baseRing = ring;
        this.euclideanAlgorithm = new EuclideanAlgorithm<>(ring);
    }

    public Fraction<E> reduce(E n, E d) {
        E gcd = euclideanAlgorithm.applyExtended(n, d).gcd();
        return new Fraction<>(baseRing.divide(n, gcd).getFirst(),
                baseRing.divide(d, gcd).getFirst());
    }

    @Override
    public Fraction<E> invert(Fraction<E> a) {
        return reduce(a.denominator(), a.numerator());
    }

    @Override
    public Fraction<E> multiply(Fraction<E> a, Fraction<E> b) {
        E nominator = baseRing.multiply(a.numerator(), b.numerator());
        E denominator = baseRing.multiply(a.denominator(), b.denominator());
        return reduce(nominator, denominator);
    }

    @Override
    public Fraction<E> identity() {
        return reduce(baseRing.identity(), baseRing.identity());
    }

    @Override
    public String toString(Fraction<E> a) {
        return baseRing.toString(a.numerator()) + " / " + baseRing.toString(a.denominator());
    }

    @Override
    public Fraction<E> add(Fraction<E> a, Fraction<E> b) {
        E nominator = baseRing.add(baseRing.multiply(a.numerator(), b.denominator()),
                baseRing.multiply(a.denominator(), b.numerator()));
        E denominator = baseRing.multiply(a.denominator(), b.denominator());
        return reduce(nominator, denominator);
    }

    @Override
    public Fraction<E> negate(Fraction<E> a) {
        return reduce(baseRing.negate(a.numerator()), a.denominator());
    }

    @Override
    public Fraction<E> zero() {
        return reduce(baseRing.zero(), baseRing.identity());
    }

    @Override
    public boolean equals(Fraction<E> a, Fraction<E> b) {
        E lhs = baseRing.multiply(a.numerator(), b.denominator());
        E rhs = baseRing.multiply(b.numerator(), a.denominator());
        return baseRing.equals(lhs, rhs);
    }

    @Override
    public String toString() {
        return "Frac(" + baseRing + ")";
    }

}
