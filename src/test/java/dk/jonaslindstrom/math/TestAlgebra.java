package dk.jonaslindstrom.math;

import com.google.common.io.BaseEncoding;
import dk.jonaslindstrom.math.algebra.abstractions.AdditiveGroup;
import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.abstractions.Ring;
import dk.jonaslindstrom.math.algebra.algorithms.*;
import dk.jonaslindstrom.math.algebra.concretisations.*;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigIntegers;
import dk.jonaslindstrom.math.algebra.concretisations.big.BigPrimeField;
import dk.jonaslindstrom.math.algebra.elements.ComplexNumber;
import dk.jonaslindstrom.math.algebra.elements.curves.AffinePoint;
import dk.jonaslindstrom.math.algebra.elements.Fraction;
import dk.jonaslindstrom.math.algebra.elements.polynomial.GradedLexicographicalOrdering;
import dk.jonaslindstrom.math.algebra.elements.polynomial.MultivariatePolynomial;
import dk.jonaslindstrom.math.algebra.elements.polynomial.MultivariatePolynomial.Builder;
import dk.jonaslindstrom.math.algebra.elements.polynomial.MonomialOrdering;
import dk.jonaslindstrom.math.algebra.elements.Permutation;
import dk.jonaslindstrom.math.algebra.elements.polynomial.Polynomial;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.matrix.SparseMatrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.algebra.elements.word.AlgebraElement;
import dk.jonaslindstrom.math.algebra.helpers.JacobiSymbol;
import dk.jonaslindstrom.math.util.Pair;
import dk.jonaslindstrom.math.util.Parser;
import dk.jonaslindstrom.math.util.Triple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import junit.framework.Assert;
import org.junit.Test;

public class TestAlgebra {

  /**
   * Test the given field. It's assumed that <i>a</i> times <i>b</i> should be the parameter
   * <i>ab</i> in the field.
   */
  private static <E> void testField(Field<E> field, E a, E b, E ab) {
    E actualAb = field.multiply(a, b);
    System.out.println("In " + field + ", (" + a + ") × (" + b + ") = " + actualAb);
    Assert.assertTrue(field.equals(actualAb, ab));
    Assert.assertTrue(field.equals(field.multiply(ab, field.invert(a)), b));
  }

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
    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(ℤ);

    Polynomial<Integer> a = Polynomial.of(-4, 0, -2, 1);
    Polynomial<Integer> b = Polynomial.of(-3, 1);

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

  @Test
  public void testFiniteField256() {

    FiniteField GF256 = new FiniteField(2, 8);

    Polynomial<Integer> a = GF256.element(1, 1, 1, 0, 1, 0, 1);
    Polynomial<Integer> b = GF256.element(1, 1, 0, 0, 1);
    Polynomial<Integer> ab = GF256.element(0, 1, 1, 1, 1, 1, 1, 1);

    testField(GF256, a, b, ab);
  }

  @Test
  public void testFiniteField32() {

    FiniteField GF32 = new FiniteField(2, 5);

    Polynomial<Integer> a = GF32.element(1, 1, 1, 0, 1);
    Polynomial<Integer> b = GF32.element(1, 1, 0, 0, 1);
    Polynomial<Integer> ab = GF32.element(0, 1, 1, 1);

    testField(GF32, a, b, ab);
  }

  @Test
  public void testZn() {

    int n = 13;
    IntegersModuloN ℤ13 = new IntegersModuloN(n);
    System.out.println(ℤ13);

    Integer a = 11;
    Integer b = 7;
    Integer c = ℤ13.add(a, b);

    Assert.assertTrue(ℤ13.equals(c, 11 + 7 + n));
  }

  @Test
  public void testRationals() {
    Rationals ℚ = Rationals.getInstance();

    Fraction<Integer> a = Parser.parseFraction("4 / 3");
    Fraction<Integer> b = Fraction.of(12, 9);

    testField(ℚ, a, b, Fraction.of(4 * 12, 3 * 9));
  }

  @Test
  public void testComplexNumbers() {
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

//  @Test
//  public void testMatrixInversion() {
//    Random random = new Random(123);
//    int p = 13;
//    int n = 7;
//
//    PrimeField field = new PrimeField(13);
//    Matrix<Integer> a = Matrix.of(7, 7, (i,j) -> random.nextInt(p));
//    Matrix<Integer> b = new MatrixInversion<>(field).apply(a);
//
//    MatrixRing<Integer> matrixRing = new MatrixRing<>(field, n);
//    Matrix<Integer> c = matrixRing.multiply(a, b);
//
//
//    Assert.assertTrue(matrixRing.equals(c, matrixRing.getIdentity()));
//  }

  @Test
  public void testMatrixInversion() {
    Random random = new Random(123);
    int p = 13;
    int n = 7;

    PrimeField field = new PrimeField(13);
    Matrix<Integer> a = Matrix.of(7, 7, (i,j) -> random.nextInt(p));
    Matrix<Integer> b = new MatrixInversion<>(field).apply(a);

    MatrixRing<Integer> matrixRing = new MatrixRing<>(field, n);
    Matrix<Integer> c = matrixRing.multiply(a, b);

    Assert.assertTrue(matrixRing.equals(c, matrixRing.getIdentity()));
  }

  @Test
  public void testMatrixView() {

    Matrix<Integer> a = Matrix.of(7,
        1, 2, 3, 4, 5, 6, 7,
        2, 3, 4, 5, 6, 7, 11,
        3, 4, 5, 6, 1, 8, 9,
        4, 5, 6, 3, 8, 9, 1,
        5, 6, 7, 8, 9, 1, 2,
        6, 7, 8, 9, 1, 2, 3,
        7, 8, 9, 1, 2, 3, 4);

    Matrix<Integer> b = a.view().submatrix(1, 3, 2, 4);
    Assert.assertSame(b.get(0, 0), a.get(1, 2));
    Assert.assertSame(b.get(1, 1), a.get(2, 3));
  }

  @Test
  public void testSparseMatrix() {
    SparseMatrix.Builder<Integer> builder = new SparseMatrix.Builder<>(10, 10, 0);

    builder.add(0, 1, 1);
    builder.add(1, 7, 1);

    Matrix<Integer> a = builder.build();

    System.out.println(a);

    Assert.assertEquals(0, (int) a.get(0, 2));
    Assert.assertEquals(1, (int) a.get(1, 7));
  }

  @Test
  public void testPolynomialParser() {
    Polynomial<Integer> p = Polynomial.parse("1 +2x^2- 3x", "x");

    Assert.assertEquals(1, (int) p.getCoefficient(0));
    Assert.assertEquals((int) p.getCoefficient(1), -3);
    Assert.assertEquals(2, (int) p.getCoefficient(2));
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

    GaussianElimination<Integer> ero = new GaussianElimination<>(field);
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
    Permutation c = new Permutation(4, new int[]{0, 1, 2});
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

    Assert.assertEquals(1, (int) r.getCoefficient(1));

    Assert.assertEquals(3, (int) r.getCoefficient(3));
  }

  @Test
  public void testPolynomialMultiplication() {

    Ring<Integer> ℤ = Integers.getInstance();
    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(ℤ);
    Polynomial<Integer> p = Polynomial.parse("2x^2+3x-6", "x");
    Polynomial<Integer> q = Polynomial.parse("4x-5", "x");
    Polynomial<Integer> r = ℤx.multiply(p, q);

    System.out.println("(" + p + ")*(" + q + ") = " + r);

    Assert.assertEquals(30, (int) r.getCoefficient(0));
    Assert.assertEquals((int) r.getCoefficient(1), -39);
    Assert.assertEquals(2, (int) r.getCoefficient(2));
    Assert.assertEquals(8, (int) r.getCoefficient(3));
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

    Assert.assertEquals(18, (int) determinant.apply(a));
    Assert.assertEquals(-376, (int) determinant.apply(b));
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

    Random random = new Random(1234);
    Polynomial<Integer> P = Polynomial.of(field, random.nextInt(p), random.nextInt(p), random.nextInt(p));

    List<Pair<Integer, Integer>> points = new ArrayList<>();
    points.add(Pair.of(1, P.apply(1, field)));
    points.add(Pair.of(2, P.apply(2, field)));
    points.add(Pair.of(3, P.apply(3, field)));

    Function<List<Pair<Integer, Integer>>, Polynomial<Integer>> lagrange = new LagrangePolynomial<>(field);

    Polynomial<Integer> L = lagrange.apply(points);

    PolynomialRingOverRing<Integer> ℤx = new PolynomialRingOverRing<>(Integers.getInstance());


    Polynomial<Integer> F = new PolynomialInterpolation<>(field).apply(points);

    System.out.println(L);
    System.out.println(F);

    Assert.assertTrue("Expected " + P + " but got " + L, ℤx.equals(L, P));
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
          Assert.assertEquals(0, (int) dot.apply(u, v));
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
    Assert.assertEquals(power.apply(x, e).intValue(), 2187);
  }

  @Test
  public void testNTT() {
    Field<Integer> ring = new PrimeField(17);
    int a = 9;
    int n = 8;

    DiscreteFourierTransform<Integer> ntt = new DiscreteFourierTransform<>(ring, a, n);

    Vector<Integer> x = Vector.of(6, 0, 10, 7, 2, 3, 4, 2);
    Vector<Integer> X = ntt.apply(x);

    Assert.assertEquals(0, (int) X.get(0));
    Assert.assertEquals(11, (int) X.get(1));
    Assert.assertEquals(1, (int) X.get(2));
    Assert.assertEquals(11, (int) X.get(3));
    Assert.assertEquals(10, (int) X.get(4));
    Assert.assertEquals(0, (int) X.get(5));
    Assert.assertEquals(4, (int) X.get(6));
    Assert.assertEquals(11, (int) X.get(7));

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
        Assert.assertEquals(0, (int) w.get(i));
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

    System.out.println(p.toString());
  }

  @Test
  public void testChineseRemainderTheorem() {
    Integers ℤ = Integers.getInstance();

    ChineseRemainderTheorem<Integer> crt = new ChineseRemainderTheorem<>(ℤ);

    Vector<Integer> a = Vector.of(0, 1, 2, 6, 11);
    Vector<Integer> m = Vector.of(3, 4, 5, 7, 13);

    int x = crt.apply(a, m);
    System.out.println(x);

    for (int i = 0; i < a.size(); i++) {
      Assert.assertEquals((int) a.get(i), Math.floorMod(x, m.get(i)));
    }

  }

  @Test
  public void testMultivariatePolynomialDivision() {

    MultivariatePolynomial.DEFAULT_ORDERING =
        new GradedLexicographicalOrdering();

    Field<Integer> gf2 = new PrimeField(2);
    MultivariatePolynomialRing<Integer> ring = new MultivariatePolynomialRing<>(gf2, 2);

    Vector<MultivariatePolynomial<Integer>> f = Vector.of(
        new MultivariatePolynomial.Builder<>(2, gf2).add(1, 1, 1).add(1, 0, 0).build(),
        new MultivariatePolynomial.Builder<>(2, gf2).add(1, 0, 2).add(1, 0, 0).build());

    MultivariatePolynomialDivision<Integer> division =
        new MultivariatePolynomialDivision<>(ring);

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
        new GradedLexicographicalOrdering();

    Field<Integer> gf2 = new PrimeField(2);
    MultivariatePolynomialRing<Integer> ring = new MultivariatePolynomialRing<>(gf2, 3);

    Vector<MultivariatePolynomial<Integer>> g = Vector.of(
        new MultivariatePolynomial.Builder<>(3, gf2, ordering).add(1, 1, 1, 1).add(1, 1, 1, 0)
            .build(),
        new MultivariatePolynomial.Builder<>(3, gf2, ordering).add(1, 2, 1, 0).add(1, 0, 1, 1)
            .build());

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

    AdditiveGroup<AffinePoint<Integer>> E = new WeierstrassCurve<>(Fp, 1, 1);
    System.out.println(E);

    AffinePoint<Integer> P = new AffinePoint<>(0, 1);
    AffinePoint<Integer> Q = new AffinePoint<>(1, 4);

    Assert.assertTrue(E.equals(E.add(P, Q), new AffinePoint<>(8, 1)));
    Assert.assertTrue(E.equals(E.add(P, P), new AffinePoint<>(10, 7)));
    Assert.assertTrue(E.equals(E.subtract(P, Q), new AffinePoint<>(11, 2)));
  }

  @Test
  public void testCurve25519scalarDecode() {
    //https://www.rfc-editor.org/rfc/rfc7748#section-4.1
    byte[] bytes = BaseEncoding.base16().decode("4b66e9d4d1b4673c5ad22691957d6af5c11b6421e0ea01d42ca4169e7918ba0d".toUpperCase());
    BigInteger expected = new BigInteger("35156891815674817266734212754503633747128614016119564763269015315466259359304");
    org.junit.Assert.assertEquals(expected, Curve25519.decodeScalar(bytes).mod(Curve25519.P));
  }

  @Test
  public void testCurve25519pointDecode() {
    //https://www.rfc-editor.org/rfc/rfc7748#section-4.1
    byte[] bytes = BaseEncoding.base16().decode("e5210f12786811d3f4b7959d0538ae2c31dbe7106fc03c3efc4cd549c715a493".toUpperCase());
    BigInteger expected = new BigInteger("8883857351183929894090759386610649319417338800022198945255395922347792736741");
    org.junit.Assert.assertEquals(expected, Curve25519.decodePoint(bytes).mod(Curve25519.P));
  }

  @Test
  public void testCurve25519pointEncode() {
    //https://www.rfc-editor.org/rfc/rfc7748#section-4.1
    BigInteger x = Curve25519.decodePoint(BaseEncoding.base16().decode("e6db6867583030db3594c1a424b15f7c726624ec26b3353b10a903a6d0ab1c4c".toUpperCase())).mod(Curve25519.P);
    BigInteger n = Curve25519.decodeScalar(BaseEncoding.base16().decode("a546e36bf0527c9d3b16154b82465edd62144c0ac1fc5a18506a2244ba449ac4".toUpperCase())).mod(Curve25519.P);

    Field<BigInteger> field = new BigPrimeField(Curve25519.P);

    // Reconstruct the y-coordinate
    BigInteger rhs = field.add(field.power(x, 3), field.multiply(Curve25519.A, field.power(x, 2)), x);
    BigInteger y = new ModularSquareRoot(Curve25519.P).apply(rhs);

    // Compute nP on Curve25519
    AffinePoint<BigInteger> dh = new Curve25519().scale(n, new AffinePoint<>(x, y));

    BigInteger expected = Curve25519.decodePoint(BaseEncoding.base16().decode("c3da55379de9c6908e94ea4df28d084f32eccf03491c71f754b4075577a28552".toUpperCase())).mod(Curve25519.P);
    org.junit.Assert.assertEquals(expected, dh.x);
  }

  @Test
  public void testFieldExtension() {
    Rationals ℚ = Rationals.getInstance();

    AlgebraicFieldExtension<Fraction<Integer>> K = new QuadraticField(-5);

    Polynomial<Fraction<Integer>> r = Polynomial.of(ℚ, ℚ.integer(2), ℚ.integer(1));
    Polynomial<Fraction<Integer>> q = K.invert(r);
    testField(K, r, q, K.getIdentity());

  }

  @Test
  public void testJacobiSymbol() {
    JacobiSymbol jacobiSymbol = new JacobiSymbol();

    Assert.assertEquals(-1, jacobiSymbol.applyAsInt(7, 3));
    Assert.assertEquals(-1, jacobiSymbol.applyAsInt(11, 2));
    Assert.assertEquals(0, jacobiSymbol.applyAsInt(7, 0));
    Assert.assertEquals(1, jacobiSymbol.applyAsInt(9, 4));
    Assert.assertEquals(-1, jacobiSymbol.applyAsInt(59, 30));
  }

  @Test
  public void testTonelliShanks() {

    FiniteField field = new FiniteField(3, 4);
    TonelliShanks tonelliShanks = new TonelliShanks(field);

    Polynomial<Integer> a = Polynomial.of(Integers.getInstance(), 0, 2, 1, 1);
    Polynomial<Integer> x = tonelliShanks.apply(a);

    Assert.assertTrue(field.equals(a, field.multiply(x, x)));

    Polynomial<Integer> b = Polynomial.of(Integers.getInstance(), 1, 1, 1, 1);
    try {
      tonelliShanks.apply(b);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      // Should fail
    }
  }

  @Test
  public void testQuadraticEquationPrimeField() {

    PrimeField field = new PrimeField(23);

    int a = 4;
    int b = 2;
    int c = 6;

    QuadraticEquation<Integer, PrimeField> equation = QuadraticEquation.create(a, b, c, field);
    int x = equation.solve();

    Polynomial<Integer> polynomial = Polynomial.of(c, b, a);
    Assert.assertEquals(0, (int) polynomial.apply(x, field));
  }

  @Test
  public void testQuadraticEquationFiniteField() {

    FiniteField field = new FiniteField(3, 4);

    Polynomial<Integer> a = Polynomial.of(1, 2, 1, 1, 1);
    Polynomial<Integer> b = Polynomial.of(1, 1, 1, 0, 2);
    Polynomial<Integer> c = Polynomial.of(2, 2, 0, 1, 1);

    QuadraticEquation<Polynomial<Integer>, FiniteField> equation = QuadraticEquation
        .create(a, b, c, field);
    Polynomial<Integer> x = equation.solve();
    System.out.println(x);

    Polynomial<Polynomial<Integer>> polynomial = Polynomial.of(field, c, b, a);
    Polynomial<Integer> p = polynomial.apply(x, field);
    Assert.assertTrue(field.equals(field.getZero(), p));
  }

  @Test
  public void testBerlekampRabin() {
    Polynomial<Integer> f = Polynomial.of(4, 3, 2, 1);
    int p = 13;
    BerlekampRabinAlgorithm berlekampRabinAlgorithm = new BerlekampRabinAlgorithm(p);
    int x = berlekampRabinAlgorithm.apply(f);
    Assert.assertEquals(0, f.apply(x, new PrimeField(p)).intValue());
  }

  @Test
  public void testFactorisation() {
    BigInteger n = BigInteger.ONE.shiftLeft(1 << 6).subtract(BigInteger.ONE);

    Factorize factorize = new Factorize();
    List<BigInteger> factors = factorize.factor(n);

    BigInteger product = new Product<>(BigIntegers.getInstance()).apply(factors);
    Assert.assertEquals(n, product);
  }

  @Test
  public void testAKS() {
    Random random = new Random(1234);
    AKS aks = new AKS();
    BigInteger n = new BigInteger(8, random);
    Assert.assertEquals(n.isProbablePrime(1000), aks.test(n));
    n = new BigInteger(8, 1000, random);
    Assert.assertTrue(aks.test(n));
  }

  @Test
  public void testEulersTotientFunction() {
    EulersTotientFunction phi = new EulersTotientFunction();
    Assert.assertEquals(36, phi.apply(BigInteger.valueOf(57)).intValue());
    Assert.assertEquals(12, phi.apply(BigInteger.valueOf(42)).intValue());
  }

  @Test
  public void testFreeAlgebra() {
    AlgebraElement<Integer> a = AlgebraElement.fromWord(3, 0, 1);
    AlgebraElement<Integer> b = AlgebraElement.fromWord(2, 1, 0);
    FreeAlgebra<Integer, Integers> algebra = new FreeAlgebra<>(Integers.getInstance(), 2);
    System.out.println(algebra.multiply(a, b));
  }

}
