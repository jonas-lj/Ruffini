package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.Triple;

public class EuclideanAlgorithm<E> {

    private final EuclideanDomain<E> ring;

    public EuclideanAlgorithm(EuclideanDomain<E> domain) {
        this.ring = domain;
    }

    /**
     * Calculate the greatest common divisor <i>d</i> of <i>a</i> and <i>b</i>, and the coefficients
     * <i>x,y</i> of the Bezout identity <i>ax + by = d</i> .
     *
     * @return The triple <i>(d, x, y)</i>.
     */
    public Triple<E, E, E> extendedGcd(E a, E b) {
        E s_1 = ring.getZero();
        E s_0 = ring.getIdentity();
        E t_1 = ring.getIdentity();
        E t_0 = ring.getZero();
        E r_1 = b;
        E r_0 = a;

        while (!ring.equals(r_1, ring.getZero())) {
            Pair<E, E> division = ring.divisionWithRemainder(r_0, r_1);
            E q = division.getFirst();

            r_0 = r_1;
            r_1 = division.getSecond();

            E tmpS = s_1;
            s_1 = ring.add(s_0, ring.negate(ring.multiply(q, tmpS)));
            s_0 = tmpS;

            E tmpT = t_1;
            t_1 = ring.add(t_0, ring.negate(ring.multiply(q, tmpT)));
            t_0 = tmpT;
        }
        return new Triple<>(r_0, s_0, t_0);
    }

}
