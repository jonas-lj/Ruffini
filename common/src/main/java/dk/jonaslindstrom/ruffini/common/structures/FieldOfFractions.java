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
        E gcd = euclideanAlgorithm.extendedGcd(n, d).getFirst();
        return new Fraction<>(baseRing.divisionWithRemainder(n, gcd).getFirst(),
                baseRing.divisionWithRemainder(d, gcd).getFirst());
    }

    @Override
    public Fraction<E> invert(Fraction<E> a) {
        return reduce(a.getDenominator(), a.getNumerator());
    }

    @Override
    public Fraction<E> multiply(Fraction<E> a, Fraction<E> b) {
        E nominator = baseRing.multiply(a.getNumerator(), b.getNumerator());
        E denominator = baseRing.multiply(a.getDenominator(), b.getDenominator());
        return reduce(nominator, denominator);
    }

    @Override
    public Fraction<E> getIdentity() {
        return reduce(baseRing.getIdentity(), baseRing.getIdentity());
    }

    @Override
    public String toString(Fraction<E> a) {
        return baseRing.toString(a.getNumerator()) + " / " + baseRing.toString(a.getDenominator());
    }

    @Override
    public Fraction<E> add(Fraction<E> a, Fraction<E> b) {
        E nominator = baseRing.add(baseRing.multiply(a.getNumerator(), b.getDenominator()),
                baseRing.multiply(a.getDenominator(), b.getNumerator()));
        E denominator = baseRing.multiply(a.getDenominator(), b.getDenominator());
        return reduce(nominator, denominator);
    }

    @Override
    public Fraction<E> negate(Fraction<E> a) {
        return reduce(baseRing.negate(a.getNumerator()), a.getDenominator());
    }

    @Override
    public Fraction<E> getZero() {
        return reduce(baseRing.getZero(), baseRing.getIdentity());
    }

    @Override
    public boolean equals(Fraction<E> a, Fraction<E> b) {
        E lhs = baseRing.multiply(a.getNumerator(), b.getDenominator());
        E rhs = baseRing.multiply(b.getNumerator(), a.getDenominator());
        return baseRing.equals(lhs, rhs);
    }

    @Override
    public String toString() {
        return "Frac(" + baseRing + ")";
    }

}
