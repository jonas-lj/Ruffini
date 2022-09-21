package dk.jonaslindstrom.math.algebra.algorithms;

import java.math.BigInteger;
import java.util.List;
import java.util.function.UnaryOperator;

public class ModularSquareRoot implements UnaryOperator<BigInteger> {

    private final BigInteger p;
    private final int mod;

    public ModularSquareRoot(BigInteger p) {
        this.p = p;
        this.mod = p.mod(BigInteger.valueOf(8)).intValue();
        if (!List.of(1,3,5,7).contains(mod) || p.signum() == -1) {
            throw new IllegalArgumentException("Invalid modulus");
        }
    }

    @Override
    public BigInteger apply(BigInteger a) {
        switch (mod) {
            case 3:
            case 7:
                return a.modPow(p.add(BigInteger.ONE).shiftRight(2), p);

            case 5:
                BigInteger x = a.modPow(p.add(BigInteger.valueOf(3)).shiftRight(3), p);
                BigInteger c = x.multiply(x).mod(p);
                if (!c.equals(a)) {
                    x = x.multiply(BigInteger.TWO.modPow(p.subtract(BigInteger.ONE).shiftRight(2), p)).mod(p);
                }
                return x;

            case 1:
            default:
                throw new IllegalArgumentException("Now implemented");

        }
    }
}


