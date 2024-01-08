package dk.jonaslindstrom.ruffini.elliptic.structures.bn254;

import dk.jonaslindstrom.ruffini.common.abstractions.Group;
import dk.jonaslindstrom.ruffini.common.util.SamePair;
import dk.jonaslindstrom.ruffini.elliptic.algorithms.OptimalAtePairing;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.ShortWeierstrassCurveAffine;
import dk.jonaslindstrom.ruffini.finitefields.AlgebraicFieldExtension;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static dk.jonaslindstrom.ruffini.common.util.MathUtils.binaryExpansion;

/**
 * Implementation of the BN254 (aka BN128) pairing-friendly elliptic curve construction.
 */
public class BN254 {

    /**
     * Modulus of the base field.
     */
    public static BigInteger p = new BigInteger("21888242871839275222246405745257275088696311157297823662689037894645226208583", 10);

    /**
     * The base field <code>FP</code><i> = F<sub>p</sub></i>.
     */
    public static BigPrimeField FP = new BigPrimeField(p);

    /**
     * <code>FP2</code><i> = FP(u) / (u<sup>2</sup> + 1)</i> is a quadratic field extension of base field <code>FP</code>.
     */
    public static AlgebraicFieldExtension<BigInteger, BigPrimeField> FP2 =
            new AlgebraicFieldExtension<>(FP, "u", Polynomial.of(
                    FP.identity(),
                    FP.zero(),
                    FP.identity()));

    /**
     * <code>FP6</code><i> = FP2(v) / (v<sup>3</sup> - (u + 9))</i> is a cubic field extension of <code>FP2</code>.
     */
    public static AlgebraicFieldExtension<Polynomial<BigInteger>, AlgebraicFieldExtension<BigInteger, BigPrimeField>> FP6 =
            new AlgebraicFieldExtension<>(FP2, "v", Polynomial.of(
                    FP2.negate(Polynomial.of(FP.integer(9), FP.identity())),
                    FP2.zero(),
                    FP2.zero(),
                    FP2.identity()));

    /**
     * <code>FP12</code><i> = FP6(w) / (w<sup>2</sup> - v))</i> is a quadratic field extension of <code>FP6</code>.
     */
    public static AlgebraicFieldExtension<Polynomial<Polynomial<BigInteger>>,
            AlgebraicFieldExtension<Polynomial<BigInteger>, AlgebraicFieldExtension<BigInteger, BigPrimeField>>> FP12 =
            new AlgebraicFieldExtension<>(FP6, "w", Polynomial.of(
                    FP6.negate(Polynomial.of(FP2.zero(), FP2.identity())),
                    FP6.zero(),
                    FP6.identity()));
    public static Group<Polynomial<Polynomial<Polynomial<BigInteger>>>> GT = FP12;

    /**
     * Curve over <code>FP2</code> containing the G2 subgroup.
     */
    public static ShortWeierstrassCurveAffine<Polynomial<BigInteger>, ?> G2 =
            new ShortWeierstrassCurveAffine<>(FP2, FP2.zero(),
                    Polynomial.of(new BigInteger("19485874751759354771024239261021720505790618469301721065564631296452457478373"),
                            new BigInteger("266929791119991161246907387137283842545076965332900288569378510910307636690")));

    /**
     * Curve over <code>FP</code> containing the G1 subgroup.
     */
    public static ShortWeierstrassCurveAffine<BigInteger, ?> G1 =
            new ShortWeierstrassCurveAffine<>(FP, BigInteger.ZERO, BigInteger.valueOf(2));
    public static AffinePoint<Polynomial<BigInteger>> G2_GENERATOR = new AffinePoint<>(
            Polynomial.of(
                    new BigInteger("61A10BB519EB62FEB8D8C7E8C61EDB6A4648BBB4898BF0D91EE4224C803FB2B", 16),
            new BigInteger("516AAF9BA737833310AA78C5982AA5B1F4D746BAE3784B70D8C34C1E7D54CF3", 16)),
            Polynomial.of(
                    new BigInteger("21897A06BAF93439A90E096698C822329BD0AE6BDBE09BD19F0E07891CD2B9A", 16),
                    new BigInteger("EBB2B0E7C8B15268F6D4456F5F38D37B09006FFD739C9578A2D1AEC6B3ACE9B", 16))
    );
    /**
     * Order of subgroups of G1, G2 and GT
     */
    public static BigInteger q = new BigInteger("21888242871839275222246405745257275088548364400416034343698204186575808495617", 10);

    /**
     * Prime field of order <i>q</i>.
     */
    public static BigPrimeField FQ = new BigPrimeField(q);

    /**
     * Generator for the G1 subgroup of order <i>q</i>.
     */
    public static AffinePoint<BigInteger> G1_GENERATOR = new AffinePoint<>(
            p.subtract(BigInteger.ONE),
            BigInteger.ONE);

    /**
     * The parameter t
     */
    private static BigInteger t = new BigInteger("-4080000000000001", 16);

    /**
     * Binary expansion of t
     */
    private static List<Integer> ci = binaryExpansion(t.negate()).stream().map(x -> -x).collect(Collectors.toList());
    /**
     * The optimal Ate pairing which is a bilinear function <i>e: G1 x G2 &rarr; GT</i>.
     */
    public static BiFunction<AffinePoint<BigInteger>, AffinePoint<Polynomial<BigInteger>>, Polynomial<Polynomial<Polynomial<BigInteger>>>> PAIRING =
            (a, b) -> new OptimalAtePairing<>(
                    g1 -> FP2.embed(g1),
                    G2,
                    g2 -> FP12.embed(FP6.embed(g2)),
                    FP12,
                    BN254::twist,
                    p, q, 12).pairing(a, b, ci);

    public static SamePair<Polynomial<Polynomial<Polynomial<BigInteger>>>> twist(AffinePoint<BigInteger> p) {
        return new SamePair<>(
                Polynomial.of(
                        Polynomial.of(FP2.zero(), FP2.zero(), Polynomial.of(p.x())),
                        Polynomial.constant(FP2.zero())),
                Polynomial.of(
                        Polynomial.constant(FP2.zero()),
                        Polynomial.constant(Polynomial.of(p.y()))));
    }
}
