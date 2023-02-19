package dk.jonaslindstrom.ruffini.elliptic.structures.bls12381;

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
 * Implementation of the BLS12-381 pairing-friendly elliptic curve construction.
 */
public class BLS12381 {

    /**
     * Modulus of the base field.
     */
    public static BigInteger p = new BigInteger("1a0111ea397fe69a4b1ba7b6434bacd764774b84f38512bf6730d2a0f6b0f6241eabfffeb153ffffb9feffffffffaaab", 16);

    /**
     * The base field <code>FP</code><i> = F<sub>p</sub></i>.
     */
    public static BigPrimeField FP = new BigPrimeField(p);

    /**
     * <code>FP2</code><i> = FP(u) / (u<sup>2</sup> + 1)</i> is a quadratic field extension of base field <code>FP</code>.
     */
    public static AlgebraicFieldExtension<BigInteger, BigPrimeField> FP2 =
            new AlgebraicFieldExtension<>(FP, "u", Polynomial.of(FP,
                    FP.getIdentity(),
                    FP.getZero(),
                    FP.getIdentity()));

    /**
     * <code>FP6</code><i> = FP2(v) / (v<sup>3</sup> - (u + 1))</i> is a cubic field extension of <code>FP2</code>.
     */
    public static AlgebraicFieldExtension<Polynomial<BigInteger>, AlgebraicFieldExtension<BigInteger, BigPrimeField>> FP6 =
            new AlgebraicFieldExtension<>(FP2, "v", Polynomial.of(FP2,
                    FP2.negate(Polynomial.of(FP, FP.getIdentity(), FP.getIdentity())),
                    FP2.getZero(),
                    FP2.getZero(),
                    FP2.getIdentity()));

    /**
     * <code>FP12</code><i> = FP6(w) / (w<sup>2</sup> - v))</i> is a quadratic field extension of <code>FP6</code>.
     */
    public static AlgebraicFieldExtension<Polynomial<Polynomial<BigInteger>>,
            AlgebraicFieldExtension<Polynomial<BigInteger>, AlgebraicFieldExtension<BigInteger, BigPrimeField>>> FP12 =
            new AlgebraicFieldExtension<>(FP6, "w", Polynomial.of(FP6,
                    Polynomial.of(FP2, FP2.getZero(), FP2.negate(FP2.getIdentity())),
                    FP6.getZero(),
                    FP6.getIdentity()));
    public static Group<Polynomial<Polynomial<Polynomial<BigInteger>>>> GT = FP12;
    /**
     * Curve over <code>FP2</code> containing the G2 subgroup.
     */
    public static ShortWeierstrassCurveAffine<Polynomial<BigInteger>, ?> G2 =
            new ShortWeierstrassCurveAffine<>(FP2, FP2.getZero(), Polynomial.of(FP, BigInteger.valueOf(4), BigInteger.valueOf(4)));
    /**
     * Curve over <code>FP</code> containing the G1 subgroup.
     */
    public static ShortWeierstrassCurveAffine<BigInteger, ?> G1 =
            new ShortWeierstrassCurveAffine<>(FP, BigInteger.ZERO, BigInteger.valueOf(4));
    public static AffinePoint<Polynomial<BigInteger>> G2_GENERATOR = new AffinePoint<>(
            Polynomial.of(FP,
                    new BigInteger("024aa2b2f08f0a91260805272dc51051c6e47ad4fa403b02b4510b647ae3d1770bac0326a805bbefd48056c8c121bdb8", 16),
                    new BigInteger("13e02b6052719f607dacd3a088274f65596bd0d09920b61ab5da61bbdc7f5049334cf11213945d57e5ac7d055d042b7e", 16)),
            Polynomial.of(FP,
                    new BigInteger("0ce5d527727d6e118cc9cdc6da2e351aadfd9baa8cbdd3a76d429a695160d12c923ac9cc3baca289e193548608b82801", 16),
                    new BigInteger("0606c4a02ea734cc32acd2b02bc28b99cb3e287e85a763af267492ab572e99ab3f370d275cec1da1aaa9075ff05f79be", 16))
    );
    /**
     * Order of subgroups of G1, G2 and GT
     */
    public static BigInteger q = new BigInteger("73eda753299d7d483339d80809a1d80553bda402fffe5bfeffffffff00000001", 16);
    /**
     * Prime field of order <i>q</i>.
     */
    public static BigPrimeField FQ = new BigPrimeField(q);
    /**
     * Generator for the G1 subgroup of order <i>q</i>.
     */
    public static AffinePoint<BigInteger> G1_GENERATOR = new AffinePoint<>(
            new BigInteger("17f1d3a73197d7942695638c4fa9ac0fc3688c4f9774b905a14e3a3f171bac586c55e83ff97a1aeffb3af00adb22c6bb", 16),
            new BigInteger("08b3f481e3aaa0f1a09e30ed741d8ae4fcf5e095d5d00af600db18cb2c04b3edd03cc744a2888ae40caa232946c5e7e1", 16));
    /**
     * The parameter t
     */
    private static BigInteger t = new BigInteger("-d201000000010000", 16);
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
                    BLS12381::twist,
                    p, q, 12).pairing(a, b, ci);

    public static SamePair<Polynomial<Polynomial<Polynomial<BigInteger>>>> twist(AffinePoint<BigInteger> p) {
        return new SamePair<>(
                Polynomial.of(FP6,
                        Polynomial.of(FP2, FP2.getZero(), Polynomial.of(FP, p.x()), FP2.getZero()),
                        Polynomial.constant(FP2.getZero())),
                Polynomial.of(FP6,
                        Polynomial.constant(FP2.getZero()),
                        Polynomial.of(FP2, FP2.getZero(), Polynomial.of(FP, p.y()), FP2.getZero())));
    }
}
