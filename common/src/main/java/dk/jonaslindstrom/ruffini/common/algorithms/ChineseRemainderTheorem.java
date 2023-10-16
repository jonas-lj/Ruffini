package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.helpers.Calculator;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.function.BiFunction;

/**
 * Compute the solution to a system of congruences using the Chinese Remainder Theorem.
 *
 * @param <E> Element type.
 */
public class ChineseRemainderTheorem<E> implements BiFunction<Vector<E>, Vector<E>, E> {

    private final EuclideanDomain<E> domain;
    private final EuclideanAlgorithm<E> euclideanAlgorithm;

    public ChineseRemainderTheorem(EuclideanDomain<E> domain) {
        this.domain = domain;
        this.euclideanAlgorithm = new EuclideanAlgorithm<>(domain);
    }

    @Override
    public E apply(Vector<E> a, Vector<E> m) {
        assert (a.size() == m.size());

        if (a.size() == 1) {
            return a.get(0);
        }

        EuclideanAlgorithm.Result<E> bezout = euclideanAlgorithm.applyExtended(m.get(0), m.get(1));

        Calculator<E> c = new Calculator<>(domain);

        E x = c.sum(c.mul(a.get(0), bezout.y(), m.get(1)),
                c.mul(a.get(1), bezout.x(), m.get(0)));

        x = domain.divide(x, domain.multiply(m.get(0), m.get(1))).second;

        if (a.size() == 2) {
            return x;
        }

        Vector<E> newModulus = Vector.of(m.size() - 1, i -> {
            if (i == 0) {
                return domain.multiply(m.get(0), m.get(1));
            } else {
                return m.get(i + 1);
            }
        });

        E finalX = x;
        Vector<E> newA = Vector.of(a.size() - 1, i -> {
            if (i == 0) {
                return finalX;
            } else {
                return a.get(i + 1);
            }
        });

        return apply(newA, newModulus);
    }

}
