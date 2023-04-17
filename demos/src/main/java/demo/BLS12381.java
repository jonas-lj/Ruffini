package demo;

import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.exceptions.InvalidParametersException;

import java.security.NoSuchAlgorithmException;

import static dk.jonaslindstrom.ruffini.elliptic.structures.bls12381.BLS12381.*;

public class BLS12381 {

    public static void main(String[] arguments) throws InvalidParametersException, NoSuchAlgorithmException {

        // Check that e(5g, h) = e(g, 5h) = e(g,h)^5 as expected
        System.out.println(PAIRING.apply(G1.scale(5, G1_GENERATOR), G2_GENERATOR));
        System.out.println(PAIRING.apply(G1_GENERATOR, G2.scale(5, G2_GENERATOR)));
        System.out.println(new Power<>(GT).apply(PAIRING.apply(G1_GENERATOR, G2_GENERATOR), 5));
    }

}
