package dk.jonaslindstrom.ruffini.common.abstractions;

import dk.jonaslindstrom.ruffini.common.algorithms.Multiply;
import dk.jonaslindstrom.ruffini.common.algorithms.Sum;

import java.math.BigInteger;
import java.util.List;

/**
 * An additive group is like a {@link Group} but where the operation is commutative and is called <code>add</code>.
 *
 * @param <E> Element type.
 */
public interface AdditiveGroup<E> extends CommutativeMonoid<E> {

    /**
     * Return <i>-a</i>.
     */
    E negate(E a);

    /**
     * Compute <i>a-b</i>.
     */
    default E subtract(E a, E b) {
        return add(a, negate(b));
    }

    default boolean isZero(E a) {
        return this.equals(a, this.zero());
    }

    /**
     * Return <i>e</i> added to it self <i>n</i> times in this monoid
     */
    default E scale(BigInteger n, E e) {
        return new Multiply<>(this).apply(n, e);
    }

    /**
     * Return <i>e</i> added to it self <i>n</i> times in this monoid
     */
    default E scale(int n, E e) {
        return new Multiply<>(this).apply(n, e);
    }

    /**
     * Return <i>2e</i>. The default implementation returns <i>e + e</i> but some implementations may
     * override this with faster implementations.
     */
    default E doubling(E e) {
        return add(e, e);
    }

    /** Returns the sum of a list of elements. */
    default E sum(List<E> terms) {
        return new Sum<>(this).apply(terms);
    }
}
