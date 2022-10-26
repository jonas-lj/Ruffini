package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

import java.util.function.UnaryOperator;

public class QuadraticEquation<E, F extends Field<E>> {

    private final E a, b, c;
    private final UnaryOperator<E> squareRoot;
    private final F field;

    /**
     * Field should have characteristic != 2
     */
    public QuadraticEquation(E a, E b, E c, F field, UnaryOperator<E> squareRoot) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.field = field;
        this.squareRoot = squareRoot;
    }

    public E solve() throws IllegalArgumentException {
        IntegerRingEmbedding<E> integerEmbedding = new IntegerRingEmbedding<>(field);
        E d = field.subtract(field.multiply(b, b), field.multiply(integerEmbedding.apply(4), a, c));
        E sqrt;
        try {
            sqrt = squareRoot.apply(d); // Throws exception if no square root exists.
        } catch (Exception e) {
            throw new IllegalArgumentException("Not possible to compute square root.");
        }
        return field.divide(field.subtract(sqrt, b), field.add(a, a));
    }

}
