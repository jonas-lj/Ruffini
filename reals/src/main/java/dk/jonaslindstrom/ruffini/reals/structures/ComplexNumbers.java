package dk.jonaslindstrom.ruffini.reals.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.reals.elements.ComplexNumber;

public class ComplexNumbers implements Field<ComplexNumber> {

    private static final ComplexNumbers instance = new ComplexNumbers();

    private ComplexNumbers() {

    }

    public static ComplexNumbers getInstance() {
        return instance;
    }

    @Override
    public ComplexNumber invert(ComplexNumber a) {
        double n = a.x * a.x + a.y * a.y;
        return new ComplexNumber(a.x / n, -a.y / n);
    }

    @Override
    public ComplexNumber getIdentity() {
        return new ComplexNumber(1, 0);
    }

    @Override
    public ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
    }

    @Override
    public String toString(ComplexNumber a) {
        return a.toString();
    }

    @Override
    public boolean equals(ComplexNumber a, ComplexNumber b) {
        return a.x == b.x && a.y == b.y;
    }

    @Override
    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.x + b.x, a.y + b.y);
    }

    @Override
    public ComplexNumber negate(ComplexNumber a) {
        return new ComplexNumber(-a.x, -a.y);
    }

    @Override
    public ComplexNumber getZero() {
        return new ComplexNumber(0, 0);
    }

    public String toString() {
        return "\\mathbb{C}";
    }

}
