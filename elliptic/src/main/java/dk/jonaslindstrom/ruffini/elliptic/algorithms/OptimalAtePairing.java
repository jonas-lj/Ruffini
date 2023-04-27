package dk.jonaslindstrom.ruffini.elliptic.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.util.SamePair;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.ShortWeierstrassCurveAffine;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

/**
 * Compute the optimal Ate pairing. Methodology is taken from Appendix A in draft-irtf-cfrg-pairing-friendly-curves-11.
 */
public class OptimalAtePairing<
        E1,
        E2,
        ET> {

    private final ShortWeierstrassCurveAffine<E2, ?> curve2;
    private final BigInteger p;
    private final BigInteger r;
    private final int k;
    private final Field<ET> ft;
    private final Function<E2, ET> g2embedding;
    private final Function<E1, E2> g1embedding;
    private final Function<AffinePoint<E1>, SamePair<ET>> twist;

    /**
     * @param g1embedding Embedding from F1 to F2.
     * @param curve2      Elliptic curve over F2.
     * @param g2embedding Embedding from F2 to FT.
     * @param ft          The field extension FT = F1<sup>k</sup>. Note that the target group GT is the multiplicative group of FT.
     * @param twist       A function computing the isomorphism from E1 to the twist E' over FT.
     * @param p           The characteristic of the fields.
     * @param r           The order of the groups G1, G2 and GT.
     * @param k           The embedding degree of the elliptic curve E1 over F1, eg. the smallest <i>k</i> such that <i>r | p<sup>k</sup>-1</i>.
     */
    public OptimalAtePairing(Function<E1, E2> g1embedding,
                             ShortWeierstrassCurveAffine<E2, ?> curve2,
                             Function<E2, ET> g2embedding,
                             Field<ET> ft,
                             Function<AffinePoint<E1>, SamePair<ET>> twist,
                             BigInteger p, BigInteger r, int k) {
        this.curve2 = curve2;
        this.ft = ft;
        this.p = p; // Characteristic of the fields
        this.r = r; // Order of G1
        this.k = k; // Embedding degree
        this.g1embedding = g1embedding;
        this.g2embedding = g2embedding;
        this.twist = twist;
    }

    public ET lineFunction(AffinePoint<E2> Q1,
                           AffinePoint<E2> Q2,
                           AffinePoint<E1> P) {
        SamePair<ET> twist = this.twist.apply(P);

        E2 l;
        Field<E2> field = curve2.getField();
        if (curve2.equals(Q1, Q2)) {
            l = field.divide(field.multiply(3, field.multiply(Q1.x(), Q1.x())), field.multiply(2, Q1.y()));
        } else if (curve2.equals(Q1, curve2.negate(Q2))) {
            return ft.subtract(twist.first, g2embedding.apply(Q1.x()));
        } else {
            l = field.divide(field.subtract(Q2.y(), Q1.y()), field.subtract(Q2.x(), Q1.x()));
        }

        return ft.add(ft.multiply(g2embedding.apply(l), ft.subtract(twist.first, g2embedding.apply(Q1.x()))),
                ft.subtract(g2embedding.apply(Q1.y()), twist.second));
    }

    public ET pairing(AffinePoint<E1> P, AffinePoint<E2> Q, List<Integer> ci) {
        ET f = ft.identity();
        AffinePoint<E2> T = Q;

        int L = ci.size() - 1;
        if (ci.get(L) == -1) {
            T = curve2.negate(T);
        }
        AffinePoint<E2> mQ = curve2.negate(Q);

        for (int i = L - 1; i >= 0; i--) {
            f = ft.multiply(f, f, lineFunction(T, T, P));
            T = curve2.add(T, T);
            if (ci.get(i) == 1) {
                f = ft.multiply(f, lineFunction(T, Q, P));
                T = curve2.add(T, Q);
            } else if (ci.get(i) == -1) {
                f = ft.multiply(f, lineFunction(T, mQ, P));
                T = curve2.subtract(T, Q);
            }

        }
        BigInteger exponent = p.pow(k).subtract(BigInteger.ONE).divide(r);
        f = new Power<>(ft).apply(f, exponent);
        return f;
    }

}
