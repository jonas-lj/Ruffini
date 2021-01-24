package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.EuclideanDomain;
import dk.jonaslindstrom.math.algebra.elements.vector.ConstructiveVector;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.algebra.helpers.Calculator;
import dk.jonaslindstrom.math.util.Triple;
import java.util.function.BiFunction;

public class ChineseRemainderTheorem<E> implements BiFunction<Vector<E>, Vector<E>, E> {

  private final EuclideanDomain<E> domain;
  private final EuclideanAlgorithm<E> euclideanAlgorithm;

  public ChineseRemainderTheorem(EuclideanDomain<E> domain) {
    this.domain = domain;
    this.euclideanAlgorithm = new EuclideanAlgorithm<>(domain);
  }

  @Override
  public E apply(Vector<E> a, Vector<E> m) {
    assert (a.getDimension() == m.getDimension());

    if (a.getDimension() == 1) {
      return a.get(0);
    }

    Triple<E, E, E> bezout = euclideanAlgorithm.extendedGcd(m.get(0), m.get(1));

    Calculator<E> c = new Calculator<>(domain);

    E x = c.sum(c.mul(a.get(0), bezout.third, m.get(1)),
        c.mul(a.get(1), bezout.second, m.get(0)));

    if (a.getDimension() == 2) {
      return x;
    }

    Vector<E> newModulus = new ConstructiveVector<>(m.getDimension() - 1, i -> {
      if (i == 0) {
        return domain.multiply(m.get(0), m.get(1));
      } else {
        return m.get(i + 1);
      }
    });

    Vector<E> newA = new ConstructiveVector<>(a.getDimension() - 1, i -> {
      if (i == 0) {
        return x;
      } else {
        return a.get(i + 1);
      }
    });

    return apply(newA, newModulus);
  }

}
