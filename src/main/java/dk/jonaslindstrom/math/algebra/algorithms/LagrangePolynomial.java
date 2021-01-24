package dk.jonaslindstrom.math.algebra.algorithms;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRing;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Compute the Lagrange interpolation polynomial which is the polynomial of lowest degree which
 * assumes a given set of points.
 */
public class LagrangePolynomial<E> implements Function<List<Vector<E>>, Polynomial<E>> {

  private final Field<E> field;

  public LagrangePolynomial(Field<E> field) {
    this.field = field;
  }

  @Override
  public Polynomial<E> apply(List<Vector<E>> points) {

    PolynomialRing<E> ring = new PolynomialRing<>(field);
    int k = points.size();

    List<Polynomial<E>> lj = new ArrayList<>();
    for (int j = 0; j < k; j++) {
      Polynomial<E> l = ring.getIdentity();
      E xj = points.get(j).get(0);
      for (int m = 0; m < k; m++) {
        if (m == j) {
          continue;
        }
        E xm = points.get(m).get(0);

        Polynomial<E> f = Polynomial.of(field, field.negate(xm), field.getIdentity());
        f = f.scale(field.invert(field.subtract(xj, xm)), field);
        l = ring.multiply(l, f);
      }
      lj.add(l);
    }

    Polynomial<E> L = ring.getZero();
    for (int j = 0; j < k; j++) {
      E yj = points.get(j).get(1);
      L = ring.add(L, lj.get(j).scale(yj, field));
    }

    return L;
  }

}
