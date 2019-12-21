package dk.jonaslindstrom.math;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.CharacteristicPolynomial;
import dk.jonaslindstrom.math.algebra.algorithms.ChineseRemainderTheorem;
import dk.jonaslindstrom.math.algebra.algorithms.Determinant;
import dk.jonaslindstrom.math.algebra.algorithms.DiscreteFourierTransform;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.math.algebra.algorithms.GramMatrix;
import dk.jonaslindstrom.math.algebra.algorithms.GramSchmidtOverRing;
import dk.jonaslindstrom.math.algebra.algorithms.GröbnerBasis;
import dk.jonaslindstrom.math.algebra.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.math.algebra.algorithms.InverseDiscreteFourierTransform;
import dk.jonaslindstrom.math.algebra.algorithms.LagrangePolynomial;
import dk.jonaslindstrom.math.algebra.algorithms.MatrixMultiplication;
import dk.jonaslindstrom.math.algebra.algorithms.MultivariatePolynomialDivision;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.algorithms.ReducedRowEchelonForm;
import dk.jonaslindstrom.math.algebra.algorithms.StrassenMultiplication;
import dk.jonaslindstrom.math.algebra.concretisations.ComplexNumbers;
import dk.jonaslindstrom.math.algebra.concretisations.EllipticCurve;
import dk.jonaslindstrom.math.algebra.concretisations.FiniteField;
import dk.jonaslindstrom.math.algebra.concretisations.Integers;
import dk.jonaslindstrom.math.algebra.concretisations.IntegersModuloN;
import dk.jonaslindstrom.math.algebra.concretisations.MatrixRing;
import dk.jonaslindstrom.math.algebra.concretisations.MultivariatePolynomialRing;
import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRing;
import dk.jonaslindstrom.math.algebra.concretisations.PolynomialRingOverRing;
import dk.jonaslindstrom.math.algebra.concretisations.PrimeField;
import dk.jonaslindstrom.math.algebra.concretisations.Rationals;
import dk.jonaslindstrom.math.algebra.concretisations.RealNumbers;
import dk.jonaslindstrom.math.algebra.concretisations.SymmetricGroup;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassForm;
import dk.jonaslindstrom.math.algebra.elements.ComplexNumber;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;
import dk.jonaslindstrom.math.algebra.elements.Fraction;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial.Builder;
import dk.jonaslindstrom.math.algebra.elements.MultivariatePolynomial.MonomialOrdering;
import dk.jonaslindstrom.math.algebra.elements.Permutation;
import dk.jonaslindstrom.math.algebra.elements.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.SparseMatrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.algebra.exceptions.InvalidParametersException;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.Parser;
import dk.jonaslindstrom.math.util.Triple;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import junit.framework.Assert;
import org.junit.Test;

public class TestAlgebra {

  @Test
  public void testGcd() {
    Integers ℤ = Integers.getInstance();
    Integer a = 240;
    Integer b = 46;
    Integer gcd = 2; // expected

    Triple<Integer, Integer, Integer> result = new EuclideanAlgorithm<>(ℤ).extendedGcd(a, b);

    Assert.assertTrue(ℤ.equals(result.getFirst(), gcd));
    Assert.assertTrue(
        ℤ.equals(ℤ.add(ℤ.multiply(result.getSecond(), a), ℤ.multiply(result.getThird(), b)), gcd));
  }

  @Test
  public void testPolynomialDivision() {
    Integers ℤ = Integers.getInstance();
    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<Integer>(ℤ);

    Polynomial<Integer> a = Polynomial.of(ℤ, -4, 0, -2, 1);
    Polynomial<Integer> b = Polynomial.of(ℤ, -3, 1);

    Pair<Polynomial<Integer>, Polynomial<Integer>> result = ℤx.divisionWithRemainder(a, b);

    System.out.println(result.first);
    System.out.println(result.second);
    Assert.assertTrue(ℤx.equals(result.first, Polynomial.of(ℤ, 3, 1, 1)));
    Assert.assertTrue(ℤx.equals(result.second, Polynomial.constant(5)));
  }

  @Test
  public void testGcdPolynomials() {
    Field<Integer> f = new PrimeField(13);
    PolynomialRing<Integer> ℤx = new PolynomialRing<>(f);

    Polynomial<Integer> a = Polynomial.of(f, 1, 2, 3);
    Polynomial<Integer> b = Polynomial.of(f, 5, 4);

    Triple<Polynomial<Integer>, Polynomial<Integer>, Polynomial<Integer>> result =
        new EuclideanAlgorithm<>(ℤx).extendedGcd(a, b);

    System.out.println(result);
  }

  @Test
  public void testFp() {
    int p = 13;
    int x = 5;
    int y = 9;

    PrimeField field = new PrimeField(p);

    testField(field, x, y, (x * y) % p);
  }

  /**
   * Test the given field. It's assumed that <i>a</i> times <i>b</i> should be the parameter
   * <i>ab</i> in the field.
   * 
   * @param field
   * @param a
   * @param b
   * @param ab
   */
  private static <E> void testField(Field<E> field, E a, E b, E ab) {
    E actualAb = field.multiply(a, b);
    System.out.println("In " + field + ", (" + a + ") × (" + b + ") = " + actualAb);
    Assert.assertTrue(field.equals(actualAb, ab));
    Assert.assertTrue(field.equals(field.multiply(ab, field.invert(a)), b));
  }

  @Test
  public void testFiniteField256() {

    FiniteField GF256 = new FiniteField(2, 8);

    Polynomial<Integer> a = GF256.element(1,1,1,0,1,0,1);
    Polynomial<Integer> b = GF256.element(1,1,0,0,1);
    Polynomial<Integer> ab = GF256.element(0,1,1,1,1,1,1,1);

    testField(GF256, a, b, ab);
  }
  
  @Test
  public void testFiniteField32() {

    FiniteField GF32 = new FiniteField(2, 5);

    Polynomial<Integer> a = GF32.element(1,1,1,0,1);
    Polynomial<Integer> b = GF32.element(1,1,0,0,1);
    Polynomial<Integer> ab = GF32.element(0,1,1,1);

    testField(GF32, a, b, ab);
  }

  @Test
  public void testZn() throws InvalidParametersException {

    int n = 13;
    IntegersModuloN ℤ13 = new IntegersModuloN(n);
    System.out.println(ℤ13);

    Integer a = 11;
    Integer b = 7;
    Integer c = ℤ13.add(a, b);

    Assert.assertTrue(ℤ13.equals(c, 11 + 7 + n));
  }

  @Test
  public void testRationals() throws InvalidParametersException {
    Rationals ℚ = Rationals.getInstance();

    Fraction<Integer> a = Parser.parseFraction("4 / 3");
    Fraction<Integer> b = Fraction.of(12, 9);

    testField(ℚ, a, b, Fraction.of(4 * 12, 3 * 9));
  }

  @Test
  public void testComplexNumbers() throws InvalidParametersException {
    ComplexNumbers ℂ = ComplexNumbers.getInstance();

    ComplexNumber a = new ComplexNumber(1, 3);
    ComplexNumber b = new ComplexNumber(2, -1);

    testField(ℂ, a, b, new ComplexNumber(5, 5));
  }

  @Test
  public void testMatrix() {

    Integers ℤ = Integers.getInstance();
    Matrix<Integer> a = Matrix.of(7, 
        1, 2, 3, 4, 5, 6, 7, 
        2, 3, 4, 5, 6, 7, 8,
        3, 4, 5, 6, 7, 8, 9,
        4, 5, 6, 7, 8, 9, 1,
        5, 6, 7, 8, 9, 1, 2,
        6, 7, 8, 9, 1, 2, 3,
        7, 8, 9, 1, 2, 3, 4);

    Matrix<Integer> b = Matrix.copy(a);

    MatrixRing<Integer> matrixRing = new MatrixRing<>(ℤ, 7);
    Matrix<Integer> c = matrixRing.multiply(a, b);

    StrassenMultiplication<Integer> strassen = new StrassenMultiplication<>(ℤ);
    Matrix<Integer> d = strassen.apply(a, b);

    Assert.assertTrue(matrixRing.equals(c, d));
  }

  @Test
  public void testMatrixView() {
    Matrix<Integer> a = Matrix.of(7, 
        1, 2, 3, 4, 5, 6, 7, 
        2, 3, 4, 5, 6, 7, 8,
        3, 4, 5, 6, 7, 8, 9,
        4, 5, 6, 7, 8, 9, 1,
        5, 6, 7, 8, 9, 1, 2,
        6, 7, 8, 9, 1, 2, 3,
        7, 8, 9, 1, 2, 3, 4);

    Matrix<Integer> b = a.view().submatrix(1, 3, 2, 4);
    Assert.assertTrue(b.get(0, 0) == a.get(1, 2));
    Assert.assertTrue(b.get(1, 1) == a.get(2, 3));
  }

  @Test
  public void testSparseMatrix() {
    SparseMatrix.Builder<Integer> builder = new SparseMatrix.Builder<>(10, 10, 0);

    builder.add(0, 1, 1);
    builder.add(1, 7, 1);

    Matrix<Integer> a = builder.build();

    System.out.println(a);

    Assert.assertTrue(a.get(0, 2) == 0);
    Assert.assertTrue(a.get(1, 7) == 1);
  }

  @Test
  public void testPolynomialParser() {
    Polynomial<Integer> p = Polynomial.parse("1 +2x^2- 3x", "x");

    Assert.assertTrue(p.getCoefficient(0) == 1);
    Assert.assertTrue(p.getCoefficient(1) == -3);
    Assert.assertTrue(p.getCoefficient(2) == 2);
  }

  @Test
  public void testLA() {
    PrimeField field = new PrimeField(7);
    Matrix<Integer> a = Matrix.of(4, 
        1, 2, 3, 1, 
        4, 1, 2, 2, 
        3, 4, 4, 0, 
        3, 1, 4, 4);

    MatrixRing<Integer> M = new MatrixRing<>(field, 3);
    System.out.println(M);
    Matrix<Integer> b = M.multiply(a, a);
    System.out.println(b);

    ReducedRowEchelonForm<Integer> ero = new ReducedRowEchelonForm<>(field);
    Matrix<Integer> c = ero.apply(a);

    System.out.println(c);

  }

  @Test
  public void testSN() {
    SymmetricGroup sn = new SymmetricGroup(4);
    System.out.println(sn);
    Permutation p = new Permutation(1, 2, 0, 3);
    System.out.println(p);
    Assert.assertTrue(sn.equals(new Permutation(4), sn.multiply(p, sn.invert(p))));

    // Cycle on the first three symbols - should be equal to p
    Permutation c = new Permutation(4, new int[] {0, 1, 2});
    System.out.println(c);
    Assert.assertTrue(sn.equals(p, c));
  }

  @Test
  public void testPolynomialAddition() {

    Ring<Integer> ℤ = Integers.getInstance();
    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(ℤ);
    Polynomial<Integer> p = Polynomial.parse("x^8+x^3+x^2+x", "x");
    Polynomial<Integer> q = Polynomial.parse("x^6+2x^3+1", "x");
    Polynomial<Integer> r = ℤx.add(p, q);

    System.out.println("(" + p + ")+(" + q + ") = " + r);

    Assert.assertTrue(r.getCoefficient(1) == 1);

    Assert.assertTrue(r.getCoefficient(3) == 3);
  }

  @Test
  public void testPolynomialMultiplication() {

    Ring<Integer> ℤ = Integers.getInstance();
    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(ℤ);
    Polynomial<Integer> p = Polynomial.parse("2x^2+3x-6", "x");
    Polynomial<Integer> q = Polynomial.parse("4x-5", "x");
    Polynomial<Integer> r = ℤx.multiply(p, q);

    System.out.println("(" + p + ")*(" + q + ") = " + r);

    Assert.assertTrue(r.getCoefficient(0) == 30);
    Assert.assertTrue(r.getCoefficient(1) == -39);
    Assert.assertTrue(r.getCoefficient(2) == 2);
    Assert.assertTrue(r.getCoefficient(3) == 8);
  }

  @Test
  public void testRealNumbers() {
    RealNumbers ℝ = RealNumbers.getInstance();

    double x = 1.2;
    double y = 4.73;

    double z = ℝ.multiply(y, ℝ.invert(x));
    double e = y / x;
    Assert.assertTrue(ℝ.equals(e, z));
  }

  @Test
  public void testMinor() {
    Matrix<Integer> a = Matrix.of(3, -2, 2, -3, -1, 1, 3, 2, 0, -1);

    Matrix<Integer> b = Matrix.of(2, -2, -3, 2, -1);

    System.out.println(a);
    System.out.println(a.minor(1, 1));
    Assert.assertEquals(b, a.minor(1, 1));
  }

  @Test
  public void testDeterminant() {
    Integers ℤ = Integers.getInstance();
    Matrix<Integer> a = Matrix.of(3, 
        -2, 2, -3, 
        -1, 1, 3, 
        2, 0, -1);

    Matrix<Integer> b = Matrix.of(4, 
        1, 3, 5, 9, 
        1, 3, 1, 7, 
        4, 3, 9, 7, 
        5, 2, 0, 9);

    Function<Matrix<Integer>, Integer> determinant = new Determinant<>(ℤ);

    Integer d = determinant.apply(a);
    Integer expected = 18;
    Assert.assertEquals(expected, d);

    d = determinant.apply(b);
    expected = -376;
    Assert.assertEquals(expected, d);
  }

  @Test
  public void testCharacteristicPolynomial() {
    Integers ℤ = Integers.getInstance();
    Matrix<Integer> a = Matrix.of(3, 1, 2, 1, 6, -1, 0, -1, -2, -1);

    Function<Matrix<Integer>, Polynomial<Integer>> computeCharacteristicPolynomial =
        new CharacteristicPolynomial<>(ℤ);

    Polynomial<Integer> p = computeCharacteristicPolynomial.apply(a);

    Polynomial<Integer> e = Polynomial.of(ℤ, 0, -12, 1, 1);

    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(ℤ);
    Assert.assertTrue("Expected " + e + " but got " + p, ℤx.equals(e, p));
  }

  @Test
  public void testLagrangePolynomial() {
    int p = 13;
    PrimeField field = new PrimeField(p);

    List<Vector<Integer>> points = new ArrayList<>();
    points.add(Vector.of(1, 1));
    points.add(Vector.of(2, 4));
    points.add(Vector.of(3, 9));

    Function<List<Vector<Integer>>, Polynomial<Integer>> lagrange = new LagrangePolynomial<>(field);

    Polynomial<Integer> L = lagrange.apply(points);

    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(Integers.getInstance());
    Polynomial<Integer> e = Polynomial.monomial(1, 2);

    Assert.assertTrue("Expected " + e + " but got " + L, ℤx.equals(L, e));
  }

  @Test
  public void testGramMatrix() {

    Integers ℤ = Integers.getInstance();

    int m = 16;
    int n = 3;

    Random random = new Random(123);
    Matrix<Integer> a = Matrix.of(m, n, (i, j) -> random.nextInt(20));

    Matrix<Integer> c = new MatrixMultiplication<>(ℤ).apply(a, a.transpose());

    GramMatrix<Integer> strassen = new GramMatrix<>(ℤ);
    Matrix<Integer> d = strassen.apply(a);

    Assert.assertTrue(c.equals(d, Integer::equals));
  }

  @Test
  public void testGramSchmidtOverRing() {
    List<Vector<Integer>> V = new ArrayList<>();
    V.add(Vector.of(1, -1, 1, 7));
    V.add(Vector.of(1, 0, 1, 3));
    V.add(Vector.of(-1, 1, 2, 11));
    V.add(Vector.of(-1, 1, 2, 12));

    GramSchmidtOverRing<Integer> gs = new GramSchmidtOverRing<>(Integers.getInstance());
    List<Vector<Integer>> U = gs.apply(V);

    DotProduct<Integer> dot = new DotProduct<>(Integers.getInstance());
    for (Vector<Integer> u : U) {
      for (Vector<Integer> v : U) {
        if (u != v) {
          Assert.assertTrue(0 == dot.apply(u, v));
        }
      }
    }
  }

  @Test
  public void testIntegerEmbedding() {
    int x = 17;
    IntegerRingEmbedding<Integer> f = new IntegerRingEmbedding<>(Integers.getInstance());

    Assert.assertEquals(x, f.apply(x).intValue());
  }

  @Test
  public void testPower() {
    int x = 3;
    int e = 7;
    Power<Integer> power = new Power<>(Integers.getInstance());
    Assert.assertTrue(power.apply(x, e) == Math.pow(x, e));
  }

  @Test
  public void testNTT() {
    Field<Integer> ring = new PrimeField(17);
    int a = 9;
    int n = 8;

    DiscreteFourierTransform<Integer> ntt = new DiscreteFourierTransform<>(ring, a, n);

    Vector<Integer> x = Vector.of(6, 0, 10, 7, 2, 3, 4, 2);
    Vector<Integer> X = ntt.apply(x);

    Assert.assertTrue(X.get(0) == 0);
    Assert.assertTrue(X.get(1) == 11);
    Assert.assertTrue(X.get(2) == 1);
    Assert.assertTrue(X.get(3) == 11);
    Assert.assertTrue(X.get(4) == 10);
    Assert.assertTrue(X.get(5) == 0);
    Assert.assertTrue(X.get(6) == 4);
    Assert.assertTrue(X.get(7) == 11);

    InverseDiscreteFourierTransform<Integer> inv =
        new InverseDiscreteFourierTransform<>(ring, a, n);
    Vector<Integer> y = inv.apply(X);

    for (int i = 0; i < n; i++) {
      Assert.assertTrue(ring.equals(x.get(i), y.get(i)));
    }
  }

  @Test
  public void testFastPolynomialMultiplication() {

    Field<Integer> ℤ17 = new PrimeField(17);
    PolynomialRing<Integer> ℤx = new PolynomialRing<>(ℤ17);
    Polynomial<Integer> p = Polynomial.parse("2x^2+3x-6", "x");
    Polynomial<Integer> q = Polynomial.parse("4x^2-5", "x");
    Polynomial<Integer> r = ℤx.multiply(p, q);

    int a = 9;
    int n = 8;
    DiscreteFourierTransform<Integer> dft = new DiscreteFourierTransform<>(ℤ17, a, n);

    Vector<Integer> u = dft.apply(p.vector(0).pad(n, 0));
    Vector<Integer> v = dft.apply(q.vector(0).pad(n, 0));

    Vector<Integer> W = Vector.view(n, i -> ℤ17.multiply(u.get(i), v.get(i)));

    InverseDiscreteFourierTransform<Integer> idft =
        new InverseDiscreteFourierTransform<>(ℤ17, a, n);
    Vector<Integer> w = idft.apply(W);

    for (int i = 0; i <= r.degree(); i++) {
      if (Objects.isNull(r.getCoefficient(i))) {
        Assert.assertTrue(0 == w.get(i));
        continue;
      }
      Assert.assertEquals(w.get(i), r.getCoefficient(i));
    }
  }

  @Test
  public void testMultivariatePolynomial() {

    MultivariatePolynomial.Builder<Integer> builder = new Builder<>(3, Integers.getInstance());

    builder.add(7, 3, 1, 1);
    builder.add(-3, 3, 1, 2);
    builder.add(11, 2, 11, 0);

    MultivariatePolynomial<Integer> p = builder.build();

    System.out.println(p);
  }

  @Test
  public void testChineseRemainderTheorem() {
    Integers ℤ = Integers.getInstance();

    ChineseRemainderTheorem<Integer> crt = new ChineseRemainderTheorem<>(ℤ);

    Vector<Integer> a = Vector.of(0, 1, 2, 6, 11);
    Vector<Integer> m = Vector.of(3, 4, 5, 7, 13);

    int x = crt.apply(a, m);
    System.out.println(x);

    for (int i = 0; i < a.getDimension(); i++) {
      Assert.assertEquals((int) a.get(i), Math.floorMod(x, m.get(i)));
    }

  }
  
  @Test
  public void testMultivariatePolynomialDivision() {

    MultivariatePolynomial.DEFAULT_ORDERING =
        new MultivariatePolynomial.GradedLexicographicalOrdering();
    
    Field<Integer> gf2 = new PrimeField(2);
    MultivariatePolynomialRing<Integer> ring = new MultivariatePolynomialRing<Integer>(gf2, 2);

    Vector<MultivariatePolynomial<Integer>> f = Vector.of(
        new MultivariatePolynomial.Builder<>(2, gf2).add(1, 1, 1).add(1, 0, 0).build(),
        new MultivariatePolynomial.Builder<>(2, gf2).add(1, 0, 2).add(1, 0, 0).build());
    
    MultivariatePolynomialDivision<Integer> division =
        new MultivariatePolynomialDivision<Integer>(ring);

    MultivariatePolynomial<Integer> g =
        new MultivariatePolynomial.Builder<>(2, gf2).add(1, 2, 1).add(1, 1, 2).add(1, 0, 2).build();

    Pair<Vector<MultivariatePolynomial<Integer>>, MultivariatePolynomial<Integer>> result =
        division.apply(g, f);

    // Compute the linear combination of the result and the dividends

    MultivariatePolynomial<Integer> sum = new DotProduct<>(ring).apply(f, result.first);    
    sum = ring.add(sum, result.second);
    Assert.assertTrue(ring.equals(sum, g));
  }
  
  @Test
  public void testGröbnerBasis() {
    MonomialOrdering ordering =
        new MultivariatePolynomial.GradedLexicographicalOrdering();
    
    Field<Integer> gf2 = new PrimeField(2);
    MultivariatePolynomialRing<Integer> ring = new MultivariatePolynomialRing<Integer>(gf2, 3);
    
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
  
  @Test
  public void testEllipticCurve() {
    int p = 13;
    PrimeField Fp = new PrimeField(p);
    
    EllipticCurve<Integer> E = new WeierstrassForm<Integer>(Fp, 1, 1);
    System.out.println(E);
    
    ECPoint<Integer> P = new ECPoint<>(0,1);
    ECPoint<Integer> Q = new ECPoint<>(1,4);
    
    Assert.assertTrue(E.equals(E.add(P, Q), new ECPoint<>(8, 1)));
    Assert.assertTrue(E.equals(E.add(P, P), new ECPoint<>(10, 7)));    
    Assert.assertTrue(E.equals(E.subtract(P, Q), new ECPoint<>(11, 2)));
  }
}
