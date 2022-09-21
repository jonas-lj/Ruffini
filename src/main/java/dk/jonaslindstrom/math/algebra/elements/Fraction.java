package dk.jonaslindstrom.math.algebra.elements;

public class Fraction<E> {

  private final E numerator, denominator;

  public Fraction(E numerator, E denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public static <F> Fraction<F> of(F numerator, F denominator) {
    return new Fraction<>(numerator, denominator);
  }

  public E getNumerator() {
    return numerator;
  }

  public E getDenominator() {
    return denominator;
  }

  @Override
  public String toString() {
    if (denominator.toString().equals("1")) {
      return numerator.toString();
    }
    return "\\frac{" + numerator + "}{" + denominator + "}";
  }
}
