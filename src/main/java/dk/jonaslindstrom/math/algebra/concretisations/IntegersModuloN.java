package dk.jonaslindstrom.math.algebra.concretisations;

/**
 * This class is an implementation of <i>ℤ / nℤ</i>, eg. the integers <i>ℤ</i> modulo the principal
 * ideal generated by a given integer <i>n</i>.
 *
 * @author jonas
 */
public class IntegersModuloN extends QuotientRing<Integer> {

  private final Integer n;

  public IntegersModuloN(Integer n) {
    super(Integers.getInstance(), n);
    this.n = n;
  }

  public int getModulus() {
    return super.mod;
  }

}
