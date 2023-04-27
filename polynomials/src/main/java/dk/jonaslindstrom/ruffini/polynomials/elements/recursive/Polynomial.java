package dk.jonaslindstrom.ruffini.polynomials.elements.recursive;

import com.google.common.collect.ImmutableSortedMap;
import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Polynomial<S> implements BiFunction<List<S>, Ring<S>, S> {

    private final int variables;
    private final SortedMap<Integer, Polynomial<S>> coefficients;

    protected Polynomial(int variables) {
        this.variables = variables;
        this.coefficients = variables == 0 ? null : new TreeMap<>();
    }

    private Polynomial(int variables, Map<Integer, Polynomial<S>> coefficients, AdditiveGroup<S> group) {
        this.variables = variables;
        this.coefficients = coefficients.keySet().stream().filter(i -> !coefficients.get(i).isZero(group)).collect(
                ImmutableSortedMap.toImmutableSortedMap(
                        Integer::compare,
                        i -> i,
                        coefficients::get
                ));
    }

    private Polynomial(int variables, SortedMap<Integer, Polynomial<S>> coefficients) {
        this.variables = variables;
        this.coefficients = coefficients;
    }

    /**
     * Create a new polynomial with the given number of variables.
     */
    public static <T> Polynomial<T> create(int variables) {
        return variables == 0 ? new Constant<>() : new Polynomial<>(variables);
    }

    /**
     * Add this polynomial with another and return the result.
     */
    public Polynomial<S> add(Polynomial<S> other, AdditiveGroup<S> group) {
        if (this.variables != other.variables) {
            throw new IllegalArgumentException();
        }
        SortedMap<Integer, Polynomial<S>> coefficients = new TreeMap<>(this.coefficients);
        other.coefficients.keySet().forEach(i -> coefficients.merge(i, other.coefficients.get(i), (a, b) -> a.add(b, group)));
        return new Polynomial<>(this.variables, coefficients, group);
    }

    /**
     * Multiply this polynomial with another and return the result.
     */
    public Polynomial<S> multiply(Polynomial<S> other, Ring<S> ring) {
        if (this.variables != other.variables) {
            throw new IllegalArgumentException();
        }
        SortedMap<Integer, Polynomial<S>> coefficients = this.coefficients.keySet().stream().flatMap(i -> other.coefficients.keySet().stream().map(j ->
                new Pair<>(i + j, this.coefficients.get(i).multiply(other.coefficients.get(j), ring)))).collect(
                ImmutableSortedMap.toImmutableSortedMap(
                        Integer::compare,
                        Pair::getFirst,
                        Pair::getSecond,
                        (a, b) -> a.add(b, ring)));
        return new Polynomial<>(variables, coefficients, ring);
    }

    protected Stream<Pair<S, LinkedList<Integer>>> getTerms() {
        return coefficients.keySet().stream().flatMap(i -> coefficients.get(i).getTerms().map(term -> {
            term.second.push(i);
            return term;
        }));
    }

    /**
     * Returns a stream of the monomials/terms of the polynomial in increasing lexicographical order.
     */
    public Stream<Monomial<S>> terms() {
        return getTerms().map(term -> new Monomial<>(term.first, term.second));
    }

    public S get(List<Integer> indices) {
        Polynomial<S> coefficient = coefficients.get(indices.get(0));
        if (coefficient == null) {
            return null;
        }
        return coefficient.get(indices.subList(1, indices.size()));
    }

    public void set(S value, List<Integer> indices) {
        this.coefficients.putIfAbsent(indices.get(0), Polynomial.create(indices.size() - 1));
        coefficients.get(indices.get(0)).set(value, indices.subList(1, indices.size()));
    }

    public void set(S value, Integer... indices) {
        set(value, Arrays.asList(indices));
    }

    @Override
    public S apply(List<S> x, Ring<S> ring) {
        Power<S> power = new Power<>(ring);
        if (x.size() != this.variables) {
            throw new IllegalArgumentException();
        }
        return coefficients.keySet().stream().map(i -> ring.multiply(power.apply(x.get(0), i),
                coefficients.get(i).apply(x.subList(1, x.size()), ring))).reduce(ring.zero(), ring::add);
    }

    public boolean isZero(AdditiveGroup<S> group) {
        return coefficients.isEmpty() || coefficients.values().stream().allMatch(p -> p.isZero(group));
    }

    public Polynomial<S> zero(int variables) {
        return new Polynomial<>(variables);
    }

    protected Pair<Polynomial<S>, Polynomial<S>> divideInternal(Polynomial<S> d, Polynomial<S> q, Ring<S> ring) {
        if (this.isZero(ring)) {
            return new Pair<>(q, this);
        }

        int divisorDegree = d.coefficients.lastKey();
        int dividendDegree = this.coefficients.lastKey();

        if (divisorDegree > dividendDegree) {
            return new Pair<>(q, this);
        }

        Polynomial<S> coefficient = this.coefficients.get(dividendDegree);
        Polynomial<S> otherCoefficient = d.coefficients.get(divisorDegree);

        Pair<Polynomial<S>, Polynomial<S>> c = coefficient.divide(otherCoefficient, ring);

        Polynomial<S> cPrime = new Polynomial<>(variables);
        cPrime.coefficients.put(dividendDegree - divisorDegree, c.first);

        Polynomial<S> rPrime = this.add(cPrime.multiply(d, ring).replace(ring::negate), ring);
        return rPrime.divideInternal(d, q.add(cPrime, ring), ring);
    }

    protected Polynomial<S> replace(UnaryOperator<S> operation) {
        SortedMap<Integer, Polynomial<S>> c = new TreeMap<>(this.coefficients);
        c.replaceAll((i, p) -> p.replace(operation));
        return new Polynomial<>(this.variables, c);
    }

    public Pair<Polynomial<S>, Polynomial<S>> divide(Polynomial<S> other, Ring<S> ring) {
        if (this.variables != other.variables) {
            throw new IllegalArgumentException();
        }
        return divideInternal(other, zero(variables), ring);
    }

    @Override
    public String toString() {
        if (coefficients.isEmpty()) {
            return "0";
        }
        return StringUtils.sumToString(terms().map(Objects::toString).collect(Collectors.toList()));
    }
}