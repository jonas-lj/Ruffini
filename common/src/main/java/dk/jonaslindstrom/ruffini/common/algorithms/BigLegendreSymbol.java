package dk.jonaslindstrom.ruffini.common.algorithms;

import java.math.BigInteger;

/**
 * Compute the Legendre symbol of a number <i>a</i> modulo a prime <i>p</i>.
 */
public class BigLegendreSymbol {

    private final BigInteger p;

    public BigLegendreSymbol(BigInteger p) {
        this.p = p;
    }

    public int apply(BigInteger a) {
        BigInteger result = a.modPow(p.subtract(BigInteger.ONE).divide(BigInteger.TWO), p);
        if (result.equals(p.subtract(BigInteger.ONE))) {
            return -1;
        }
        return result.intValue();
    }
}
