package dk.jonaslindstrom.ruffini.reals.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

/**
 * Real numbers represented by {@link Double}s.
 */
public class RealNumbers implements Field<Double> {

    private static final RealNumbers instance = new RealNumbers();

    private RealNumbers() {
    }

    public static RealNumbers getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "\\mathbb{R}";
    }

    @Override
    public Double invert(Double a) {
        return 1.0 / a;
    }

    @Override
    public Double identity() {
        return 1.0;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public String toString(Double a) {
        return a.toString();
    }

    @Override
    public boolean equals(Double a, Double b) {
        return a.equals(b);
    }

    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double negate(Double a) {
        return -a;
    }

    @Override
    public Double zero() {
        return 0.0;
    }

}
