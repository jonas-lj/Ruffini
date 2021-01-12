package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.elements.Fraction;

public class FieldOfFractions<E> implements Field<Fraction<E>> {

  private final EuclideanDomain<E> baseRing;
  private final EuclideanAlgorithm<E> euclideanAlgorithm;

  public FieldOfFractions(EuclideanDomain<E> baseRing) {
    this.baseRing = baseRing;
    this.euclideanAlgorithm = new EuclideanAlgorithm<>(baseRing);
  }

  private Fraction<E> reduce(E n, E d) {
    E gcd = euclideanAlgorithm.extendedGcd(n, d).getFirst();
    return new Fraction<>(baseRing.divisionWithRemainder(n, gcd).getFirst(),
        baseRing.divisionWithRemainder(d, gcd).getFirst());
  }

  @Override
  public Fraction<E> invert(Fraction<E> a) {
    return reduce(a.getDenominator(), a.getNominator());
  }

  @Override
  public Fraction<E> multiply(Fraction<E> a, Fraction<E> b) {
    E nominator = baseRing.multiply(a.getNominator(), b.getNominator());
    E denominator = baseRing.multiply(a.getDenominator(), b.getDenominator());
    return reduce(nominator, denominator);
  }

  @Override
  public Fraction<E> getIdentity() {
    return reduce(baseRing.getIdentity(), baseRing.getIdentity());
  }

  @Override
  public String toString(Fraction<E> a) {
    return baseRing.toString(a.getNominator()) + " / " + baseRing.toString(a.getDenominator());
  }

  @Override
  public Fraction<E> add(Fraction<E> a, Fraction<E> b) {
    E nominator = baseRing.add(baseRing.multiply(a.getNominator(), b.getDenominator()),
        baseRing.multiply(a.getDenominator(), b.getNominator()));
    E denominator = baseRing.multiply(a.getDenominator(), b.getDenominator());
    return reduce(nominator, denominator);
  }

  @Override
  public Fraction<E> negate(Fraction<E> a) {
    return reduce(baseRing.negate(a.getNominator()), a.getDenominator());
  }

  @Override
  public Fraction<E> getZero() {
    return reduce(baseRing.getZero(), baseRing.getIdentity());
  }

  @Override
  public boolean equals(Fraction<E> a, Fraction<E> b) {
    E lhs = baseRing.multiply(a.getNominator(), b.getDenominator());
    E rhs = baseRing.multiply(b.getNominator(), a.getDenominator());
    return baseRing.equals(lhs, rhs);
  }

  @Override
  public String toString() {
    return "Frac(" + baseRing + ")";
  }

  @Override
  public int getCharacteristics() {
    return baseRing.getCharacteristics();
  }
}
