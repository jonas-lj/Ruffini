package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.math.BigInteger;

public interface EuclideanDomain<E> extends Ring<E> {

    /**
     * Returns a pair <i>(q, r)</i> such that <i>a = qb + r</i> and <i>0 &le; f(r) &lt; f(b)</i> where
     * <i>f</i> is the Euclidean function (see {@link #norm(E)} for this domain.
     */
    Pair<E, E> divisionWithRemainder(E a, E b);

    /**
     * The euclidean function is a multiplicative map that maps elements of the domain to the integers
     * and is used in the division of {@link #divisionWithRemainder(E, E)}.
     */
    BigInteger norm(E a);

}
