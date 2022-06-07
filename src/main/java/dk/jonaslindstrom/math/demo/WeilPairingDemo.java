package dk.jonaslindstrom.math.demo;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.Power;
import dk.jonaslindstrom.math.algebra.algorithms.WeilPairing;
import dk.jonaslindstrom.math.algebra.concretisations.PrimeField;
import dk.jonaslindstrom.math.algebra.concretisations.WeierstrassForm;
import dk.jonaslindstrom.math.algebra.elements.ECPoint;

public class WeilPairingDemo {

  public static void main(String[] arguments) {
    int p = 631;
    int a = 30;
    int b = 34;

    Field<Integer> field = new PrimeField(p);

    WeierstrassForm<Integer> curve = new WeierstrassForm<>(field, a, b);

    // Example from Hoffstein, "Mathematical Foundations of Cryptography"
    ECPoint<Integer> P1 = new ECPoint<>(36, 60);
    ECPoint<Integer> Q1 = new ECPoint<>(121, 387);
    ECPoint<Integer> S = new ECPoint<>(0, 36);
    ECPoint<Integer> P2 = curve.add(P1, P1, P1);
    ECPoint<Integer> Q2 = curve.add(Q1, Q1, Q1, Q1);

    WeilPairing<Integer> weilPairing = new WeilPairing<>(curve);

    System.out.println("We are working over the field " + field + " and the curve " + curve);
    
    int e1 = weilPairing.apply(P1, Q1, S, 5);
    System.out.println("The Weil pairing of " + P1 + " and " + Q1 + " is " + e1);

    int e2 = weilPairing.apply(P2, Q2, S, 5);
    System.out.println("The Weil pairing of [3]" + P1 + " and [4]" + Q1 + " is " + e2);

    int e112 = new Power<>(field).apply(e1, 12);
    System.out.println("Bilinearity: " + e1 + "^12 = " + e112 + " (expected " + e2 + ")");

    int e3 = weilPairing.apply(P2, Q1, S, 5);
    System.out.println("The Weil pairing of [3]" + P1 + " and " + Q1 + " is " + e3);

    int e13 = new Power<>(field).apply(e1, 3);
    System.out.println("Bilinearity: " + e1 + "^3 = " + e13 + " (expected " + e3 + ")");

    int e4 = weilPairing.apply(P1, Q2, S, 5);
    System.out.println("The Weil pairing of " + P1 + " and [4]" + Q1 + " is " + e4);

    int e14 = new Power<>(field).apply(e1, 4);
    System.out.println("Bilinearity: " + e1 + "^4 = " + e14 + " (expected " + e4 + ")");
  }

}
