package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.helpers.Calculator;
import dk.jonaslindstrom.ruffini.common.util.Triple;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.function.BiFunction;

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

        Triple<E, E, E> bezout = euclideanAlgorithm.extendedGcd(m.get(0), m.get(1));

        Calculator<E> c = new Calculator<>(domain);

        E x = c.sum(c.mul(a.get(0), bezout.getThird(), m.get(1)),
                c.mul(a.get(1), bezout.getSecond(), m.get(0)));

        x = domain.divisionWithRemainder(x, domain.multiply(m.get(0), m.get(1))).second;

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
