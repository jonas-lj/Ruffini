package dk.jonaslindstrom.ruffini.common.elements;

/**
 * A fraction.
 *
 * @param numerator
 * @param denominator
 * @param <E>         Element type.
 */
public record Fraction<E>(E numerator, E denominator) {

    public static Fraction<Integer> parseFraction(String string) {
        String[] split = string.split("/");
        Integer n = Integer.parseInt(split[0].trim());
        Integer d = Integer.parseInt(split[1].trim());
        return new Fraction<>(n, d);
    }

    public String toString() {
        return "\\frac{" + numerator + "}{" + denominator + "}";
    }

}
