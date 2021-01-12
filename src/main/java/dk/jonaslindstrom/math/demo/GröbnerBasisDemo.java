package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.GröbnerBasis;
import dk.jonaslindstrom.math.algebra.concretisations.MultivariatePolynomialRing;
import dk.jonaslindstrom.math.algebra.concretisations.PrimeField;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial.MonomialOrdering;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import junit.framework.Assert;
import org.junit.Test;

public class GröbnerBasisDemo {

  public static void main(String[] arguments) {
      MonomialOrdering ordering =
          new MultivariatePolynomial.GradedLexicographicalOrdering();

      Field<Integer> gf2 = new PrimeField(2);
      MultivariatePolynomialRing<Integer> ring = new MultivariatePolynomialRing<>(gf2, 3);

      Vector<MultivariatePolynomial<Integer>> g = Vector.of(
          new MultivariatePolynomial.Builder<>(3, gf2, ordering).add(1, 1, 1, 1).add(1, 1, 1, 0).build(),
          new MultivariatePolynomial.Builder<>(3, gf2, ordering).add(1, 2, 1, 0).add(1, 0, 1, 1).build());

      GröbnerBasis<Integer> gröbner = new GröbnerBasis<>(ring, ordering);
      Vector<MultivariatePolynomial<Integer>> ĝ = gröbner.apply(g);

      Assert.assertTrue(
          ĝ.anyMatch(e -> ring.equals(e, new MultivariatePolynomial.Builder<>(3, gf2, ordering)
              .add(1, 0, 1, 2).add(1, 0, 1, 1).build())));
      System.out.println(ĝ);
  }

}
