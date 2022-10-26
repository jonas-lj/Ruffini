package demo;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.elliptic.algorithms.WeilPairing;
import dk.jonaslindstrom.ruffini.elliptic.elements.AffinePoint;
import dk.jonaslindstrom.ruffini.elliptic.structures.ShortWeierstrassCurveAffine;
import dk.jonaslindstrom.ruffini.finitefields.PrimeField;

public class WeilPairingDemo {

    public static void main(String[] arguments) {
        int p = 631;
        int a = 30;
        int b = 34;

        Field<Integer> field = new PrimeField(p);

        ShortWeierstrassCurveAffine<Integer> curve = new ShortWeierstrassCurveAffine<>(field, a, b);

        // Example from Hoffstein, "Mathematical Foundations of Cryptography"
        AffinePoint<Integer> P1 = new AffinePoint<>(36, 60);
        AffinePoint<Integer> Q1 = new AffinePoint<>(121, 387);
        AffinePoint<Integer> S = new AffinePoint<>(0, 36);
        AffinePoint<Integer> P2 = curve.add(P1, P1, P1);
        AffinePoint<Integer> Q2 = curve.add(Q1, Q1, Q1, Q1);

        WeilPairing<Integer> weilPairing = new WeilPairing<>(curve);

        System.out.println("We are working over the field " + field + " and the curve " + curve);

        int e1 = weilPairing.apply(P1, Q1, S, 5);
        System.out.println("The Weil pairing of " + P1 + " and " + Q1 + " is " + e1);

        int e2 = weilPairing.apply(P2, Q2, S, 5);
        System.out.println("The Weil pairing of [3]" + P1 + " and [4]" + Q1 + " is " + e2);

        // Power map over the field
        Power<Integer> power = new Power<>(field);

        int e112 = power.apply(e1, 12);
        System.out.println("Bilinearity: " + e1 + "^12 = " + e112 + " (expected " + e2 + ")");

        int e3 = weilPairing.apply(P2, Q1, S, 5);
        System.out.println("The Weil pairing of [3]" + P1 + " and " + Q1 + " is " + e3);

        int e13 = power.apply(e1, 3);
        System.out.println("Bilinearity: " + e1 + "^3 = " + e13 + " (expected " + e3 + ")");

        int e4 = weilPairing.apply(P1, Q2, S, 5);
        System.out.println("The Weil pairing of " + P1 + " and [4]" + Q1 + " is " + e4);

        int e14 = power.apply(e1, 4);
        System.out.println("Bilinearity: " + e1 + "^4 = " + e14 + " (expected " + e4 + ")");
    }

}
