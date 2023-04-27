package dk.jonaslindstrom.ruffini.integers.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.OrderedSet;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.math.BigInteger;
import java.util.Comparator;

public class Integers implements EuclideanDomain<Integer>, OrderedSet<Integer> {

    private static final Integers instance = new Integers();

    private Integers() {

    }

    public static Integers getInstance() {
        return instance;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer identity() {
        return 1;
    }

    @Override
    public String toString(Integer a) {
        return Integer.toString(a);
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer negate(Integer a) {
        return -a;
    }

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public boolean equals(Integer a, Integer b) {
        return a.equals(b);
    }

    @Override
    public Pair<Integer, Integer> divide(Integer a, Integer b) {
        int residue = Math.floorMod(a, b);
        int quotient = (a - residue) / b;
        return new Pair<>(quotient, residue);
    }

    @Override
    public String toString() {
        return "\\mathbb{Z}";
    }

    @Override
    public BigInteger norm(Integer a) {
        return BigInteger.valueOf(Math.abs(a));
    }

    @Override
    public Comparator<Integer> getOrdering() {
        return Comparator.naturalOrder();
    }
}
