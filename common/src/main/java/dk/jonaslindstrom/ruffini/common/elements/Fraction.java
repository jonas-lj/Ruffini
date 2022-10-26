package dk.jonaslindstrom.ruffini.common.elements;

public class Fraction<E> {

    private final E numerator, denominator;

    public Fraction(E n, E d) {
        this.numerator = n;
        this.denominator = d;
    }

    public static Fraction<Integer> parseFraction(String string) {
        String[] split = string.split("/");
        Integer n = Integer.parseInt(split[0].trim());
        Integer d = Integer.parseInt(split[1].trim());
        return new Fraction<>(n, d);
    }

    public E getNumerator() {
        return numerator;
    }

    public E getDenominator() {
        return denominator;
    }

    public String toString() {
        return "\\frac{" + numerator + "}{" + denominator + "}";
    }

}
