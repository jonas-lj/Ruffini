package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class DotProduct<E> implements BiFunction<Vector<E>, Vector<E>, E> {

  private BinaryOperator<E> add;
  private BinaryOperator<E> multiply;
  
  public DotProduct(Ring<E> ring) {
    this.add = ring::add;
    this.multiply = ring::multiply;
  }

  public DotProduct(BinaryOperator<E> add, BinaryOperator<E> multiplication) {
    this.add = add;
    this.multiply = multiplication;
  }
  
  @Override
  public E apply(Vector<E> a, Vector<E> b) {
    assert (a.getDimension() == b.getDimension());
    E c = multiply.apply(a.get(0), b.get(0));
    for (int i = 1; i < a.getDimension(); i++) {
      c = add.apply(c, multiply.apply(a.get(i), b.get(i)));
    }
    return c;
  }

}
