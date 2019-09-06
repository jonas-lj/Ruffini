package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.util.StringUtils;

public class PrimeField extends IntegersModuloN implements Field<Integer> {

  public PrimeField(Integer p) {
    super(p);
  }

  @Override
  public Integer invert(Integer a) {
    Integer m = new EuclideanAlgorithm<>(Integers.getInstance()).extendedGcd(a, super.mod).getSecond();
    Integer modM = Math.floorMod(m, super.mod);
    return modM;
  }

  @Override
  public String toString() {
    return "𝔽" + StringUtils.subscript(super.mod.toString());
  }

}
