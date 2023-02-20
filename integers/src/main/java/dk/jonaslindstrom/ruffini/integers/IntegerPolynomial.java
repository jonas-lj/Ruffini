package dk.jonaslindstrom.ruffini.integers;

import dk.jonaslindstrom.ruffini.integers.structures.Integers;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

public class IntegerPolynomial {

    public static Polynomial<Integer> of(Integer... coefficients) {
        return Polynomial.of(coefficients);
    }

    private static Polynomial<Integer> parsePolynomial(String string, String variable) {
        String[] terms = string.replaceAll("-", "+-").replace(" ", "").split("\\+");

        Polynomial.Builder<Integer> p = new Polynomial.Builder<>(Integers.getInstance());

        for (String term : terms) {
            String[] s = term.split(variable);

            int power = -1;
            int coefficient;

            if (s.length == 0) {
                // A term of just "x"
                power = 1;
                coefficient = 1;
            } else {
                if (s.length == 2) {
                    power = Integer.parseInt(s[1].replaceAll("\\^", ""));
                } else if (s.length == 1) {
                    if (term.contains(variable)) {
                        power = 1;
                    } else {
                        power = 0;
                    }
                }

                if (s[0].length() > 0) {
                    coefficient = Integer.parseInt(s[0]);
                } else {
                    coefficient = 1;
                }
            }
            p.set(power, coefficient);
        }

        return p.build();
    }

    public static Polynomial<Integer> parse(String polynomial, String variable) {
        return parsePolynomial(polynomial, variable);
    }

    public static Polynomial<Integer> parse(String polynomial) {
        return parsePolynomial(polynomial, "x");
    }

}
