package dk.jonaslindstrom.math.algebra.concretisations;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.util.StringUtils;

public class FiniteField extends QuotientRing<Polynomial<Integer>>
    implements Field<Polynomial<Integer>> {

  private String stringRepresentation;

  public FiniteField(int p, int n) {
    this(new PrimeField(p), getIrreduciblePolynomial(p, n));
  }
  
  public Polynomial<Integer> element(Integer ... coefficients) {
    return Polynomial.of(Integers.getInstance(), coefficients);    
  }

  /**
   * Create a finite field as a field of prime order module an irriducible polynomial.
   * 
   * @param baseField
   * @param mod
   */
  private FiniteField(PrimeField baseField, Polynomial<Integer> mod) {
    super(new PolynomialRing<>(baseField), mod);

    this.stringRepresentation = String.format("GF(%s%s)", baseField.mod.toString(),
        StringUtils.superscript(Integer.toString(mod.degree())));
  }

  @Override
  public Polynomial<Integer> invert(Polynomial<Integer> a) {
    return new EuclideanAlgorithm<>((PolynomialRing<Integer>) super.ring).extendedGcd(a, mod).second;
  }

  @Override
  public String toString() {
    return stringRepresentation;
  }

  private static Polynomial<Integer> getIrreduciblePolynomial(int p, int degree) {

    switch (p) {
      case 2:

        switch (degree) {
          case 1:
            return Polynomial.of(Integers.getInstance(), 0, 1);          
          
          case 2:
            return Polynomial.of(Integers.getInstance(), 1, 1, 1);          
            
          case 3:
            return Polynomial.of(Integers.getInstance(), 1, 0, 1, 1);          
            
          case 4:
            return Polynomial.of(Integers.getInstance(), 1, 1, 0, 0, 1);          
            
          case 5:
            return Polynomial.of(Integers.getInstance(), 1, 0, 1, 0, 0, 1);          
            
          case 6:
            return Polynomial.of(Integers.getInstance(), 1, 1, 0, 0, 0, 0, 1);                      
            
          case 7:
            return Polynomial.of(Integers.getInstance(), 1, 1, 0, 0, 0, 0, 0, 1);                      
            
          case 8:
            return Polynomial.of(Integers.getInstance(), 1, 1, 0, 1, 1, 0, 0, 0, 1);          

          default:
            return null;
        }

      default:
        return null;

    }

  }

}
