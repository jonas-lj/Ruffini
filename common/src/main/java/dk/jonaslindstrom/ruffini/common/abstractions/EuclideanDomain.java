package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.math.BigInteger;
import java.util.Comparator;

public interface EuclideanDomain<E> extends Ring<E> {

    /**
     * Returns a pair <i>(q, r)</i> such that <i>a = qb + r</i> and <i>0 &le; f(r) &lt; f(b)</i> where
     * <i>f</i> is the Euclidean function (see {@link #norm(E)} for this domain.
     */
    Pair<E, E> divide(E a, E b);

    /**
     * Returns a pair <i>(q, r)</i> such that <i>a = qb + r</i> and <i>0 &le; f(r) &lt; f(b)</i> where
     * <i>f</i> is the Euclidean function (see {@link #norm(E)} for this domain.
     */
    default Pair<E, E> divide(E a, int b) {
        return divide(a, integer(b));
    }

    /**
     * Compute  <i>a/b</i> and throw and exception if the division is not exact.
     */
    default E divideExact(E a, E b) {
        Pair<E, E> q = divide(a, b);
        if (!isZero(q.second)) {
            throw new ArithmeticException("Division is not exact");
        }
        return q.first;
    }

    /**
     * Compute <i>a/b</i> and throw and exception if the division is not exact.
     */
    default E divideExact(E a, int b) {
        return divideExact(a, integer(b));
    }

    default E mod(E a, E m) {
        return divide(a, m).second;
    }

    default E mod(E a, int m) {
        return divide(a, m).second;
    }

    /**
     * Return <i>a</i> if <i>a &ge; 0</i> according to the given ordering, otherwise return <i>-a</i>.
     */
    default E abs(E a, Comparator<E> ordering) {
        if (ordering.compare(a, zero()) < 0) {
            return negate(a);
        }
        return a;
    }

    /**
     * The euclidean function is a multiplicative map that maps elements of the domain to the integers
     * and is used in the division of {@link #divide(E, E)}.
     */
    BigInteger norm(E a);

    default boolean divides(E a, E b) {
        return isZero(divide(b, a).second);
    }

}
