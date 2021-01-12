package dk.jonaslindstrom.math.algebra.elements;

public class Fraction<E> {

  private final E nominator, denominator;

  public Fraction(E nominator, E denominator) {
    this.nominator = nominator;
    this.denominator = denominator;
  }

  public E getNominator() {
    return nominator;
  }

  public E getDenominator() {
    return denominator;
  }

  public static <F> Fraction<F> of(F nominator, F denominator) {
    return new Fraction<>(nominator, denominator);
  }

  @Override
  public String toString() {
    if (denominator.toString().equals("1")) {
      return nominator.toString();
    }
    return nominator + "/" + denominator;
  }
}
