package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.FiniteField;
import dk.jonaslindstrom.math.algebra.concretisations.PrimeField;
import dk.jonaslindstrom.math.algebra.concretisations.RealNumbers;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import java.util.function.UnaryOperator;
import org.apache.commons.math3.util.FastMath;

public class QuadraticEquation<E, F extends Field<E>> {

  private final E a, b, c;
  private final UnaryOperator<E> squareRoot;
  private final F field;

  private QuadraticEquation(E a, E b, E c, F field, UnaryOperator<E> squareRoot) {
    if (field.getCharacteristics() == 2) {
      throw new IllegalArgumentException("Cannot solve quadratic equations over fields of characteristics 2.");
    }

    this.a = a;
    this.b = b;
    this.c = c;
    this.field = field;
    this.squareRoot = squareRoot;
  }

  public static QuadraticEquation<Polynomial<Integer>, FiniteField>  create(Polynomial<Integer> a, Polynomial<Integer> b, Polynomial<Integer> c, FiniteField field) {
    return new QuadraticEquation<>(a, b, c, field, new TonelliShanks(field));
  }

  public static QuadraticEquation<Integer, PrimeField>  create(int a, int b, int c, PrimeField field) {
    return new QuadraticEquation<>(a, b, c, field, TonelliShanks.forPrimeField(a, b, c, field));
  }

  public static QuadraticEquation<Double, RealNumbers>  create(double a, double b, double c) {
    return new QuadraticEquation<>(a, b, c, RealNumbers.getInstance(), FastMath::sqrt);
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
