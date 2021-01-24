package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.exceptions.NotInvertibleException;
import dk.jonaslindstrom.math.util.StringUtils;
import dk.jonaslindstrom.math.util.Triple;

public class PrimeField extends IntegersModuloN implements Field<Integer> {

  public PrimeField(Integer p) {
    super(p);
  }

  @Override
  public Integer invert(Integer a) {
    Triple<Integer, Integer, Integer> gcd = new EuclideanAlgorithm<>(Integers.getInstance())
        .extendedGcd(a, super.mod);
    if (gcd.first != 1) {
      throw new NotInvertibleException(super.mod);
    }
    Integer m = gcd.getSecond();
    return Math.floorMod(m, super.mod);
  }

  @Override
  public String toString() {
    return "𝔽" + StringUtils.subscript(super.mod.toString());
  }

  public FiniteField asFiniteField() {
    return new FiniteField(this, Polynomial.monomial(1, 1));
  }

}
