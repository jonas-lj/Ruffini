package dk.jonaslindstrom.ruffini.polynomials.elements;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.IntegerRingEmbedding;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.util.StringUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.ordering.LexicographicalOrdering;
import dk.jonaslindstrom.ruffini.polynomials.ordering.MonomialOrdering;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class MultivariatePolynomial<E> implements BiFunction<Vector<E>, Ring<E>, E> {

    public static MonomialOrdering DEFAULT_ORDERING = new LexicographicalOrdering();

    private final SortedMap<Monomial, E> terms;
    private final int variables;

    private MultivariatePolynomial(int variables, SortedMap<Monomial, E> terms) {
        this.variables = variables;
        this.terms = terms;
    }

    /**
     * Compute the product of two polynomials
     */
    public static <T> MultivariatePolynomial<T> multiply(MultivariatePolynomial<T> a,
                                                         MultivariatePolynomial<T> b, Ring<T> ring) {
        if (a.variables != b.variables) {
            int variables = Math.max(a.variables, b.variables);
            return multiply(a.pad(variables), b.pad(variables), ring);
        }
        Builder<T> builder = new Builder<>(a.variables, ring);
        for (Monomial ai : a.terms.keySet()) {
            for (Monomial bi : b.terms.keySet()) {
                builder.add(ring.multiply(a.terms.get(ai), b.terms.get(bi)), ai.multiply(bi));
            }
        }
        return builder.build();
    }

    /**
     * Compute the sum of two polynomials
     */
    public static <T> MultivariatePolynomial<T> add(MultivariatePolynomial<T> a,
                                                    MultivariatePolynomial<T> b, Ring<T> ring) {
        if (a.variables != b.variables) {
            int variables = Math.max(a.variables, b.variables);
            return add(a.pad(variables), b.pad(variables), ring);
        }
        Builder<T> builder = new Builder<>(a.variables, ring);
        for (Pair<Monomial, T> ai : a.coefficients()) {
            builder.add(ai.second, ai.first);
        }

        for (Pair<Monomial, T> bi : b.coefficients()) {
            builder.add(bi.second, bi.first);
        }
        return builder.build();
    }

    /**
     * Create a monomial <i>a x<sub>0</sub><sup>d<sub>0</sub></sup> ... x<sub>n-1</sub><sup>d<sub>n-1</sub></sup></i>.
     */
    public static <T> MultivariatePolynomial<T> monomial(T a, int... d) {
        return monomial(a, Monomial.of(d));
    }

    /**
     * Create a monomial <i>a x<sub>0</sub><sup>d<sub>0</sub></sup> ... x<sub>n-1</sub><sup>d<sub>n-1</sub></sup></i>.
     */
    public static <T> MultivariatePolynomial<T> monomial(T a, Monomial d) {
        SortedMap<Monomial, T> terms = new TreeMap<>(DEFAULT_ORDERING);
        terms.put(d, a);
        return new MultivariatePolynomial<>(d.variables(), terms);
    }

    /**
     * Create a new constant polynomials
     */
    public static <T> MultivariatePolynomial<T> constant(T coefficient, int variables) {
        return monomial(coefficient, new int[variables]);
    }

    private MultivariatePolynomial<E> pad(int variables) {
        if (variables == this.variables) {
            return this;
        }

        SortedMap<Monomial, E> newTerms = new TreeMap<>(DEFAULT_ORDERING);
        for (Monomial degree : terms.keySet()) {
            newTerms.put(degree.pad(variables), terms.get(degree));
        }
        return new MultivariatePolynomial<>(variables, newTerms);
    }

    private String getVariable(int i) {
        if (this.variables < 4) {
            if (i == 0) {
                return "x";
            } else if (i == 1) {
                return "y";
            } else if (i == 2) {
                return "z";
            }
        }
        return "x_" + (i + 1);
    }

    @Override
    public String toString() {
        return toString(this::getVariable);
    }

    public String toString(IntFunction<String> variables) {
        List<String> termsAsString = terms.keySet().stream().map(monomial -> {
            String coefficient = terms.get(monomial).toString();
            if (coefficient.equals("1")) {
                return monomial.toString(variables);
            } else if (coefficient.equals("-1")) {
                return "-" + monomial.toString(variables);
            }
            return coefficient + " " + monomial.toString(variables);
        }).collect(Collectors.toList());
        return StringUtils.sumToString(termsAsString);
    }

    @Override
    public E apply(Vector<E> x, Ring<E> ring) {
        E result = ring.getZero();
        for (Monomial d : terms.keySet()) {
            E c = terms.get(d);
            result = ring.add(result, ring.multiply(c, d.applyTerm(x, ring)));
        }
        return result;
    }

    public MultivariatePolynomial<E> differentiate(int variable, Ring<E> ring) {
        IntegerRingEmbedding<E> integerEmbedding = new IntegerRingEmbedding<>(ring);

        Builder<E> builder = new Builder<>(variables, ring);
        for (Monomial monomial : terms.keySet()) {
            E coefficient = terms.get(monomial);
            if (variable >= variables || monomial.degree(variable) == 0) {
                // Constant term
                continue;
            }

            int power = monomial.degree(variable);
            E newCoefficient = ring.multiply(coefficient, integerEmbedding.apply(power));
            int[] newMonomial = Arrays.copyOf(monomial.degree, variables);
            newMonomial[variable]--;
            builder.add(newCoefficient, newMonomial);
        }
        return builder.build();
    }

    /**
     * Return the number of variables in this polynomial as defined when it was created.
     */
    public int variables() {
        return variables;
    }

    /**
     * Get the coefficient for the monomial <i>x<sub>0</sub><sup>d<sub>0</sub></sup> ... x<sub>n-1</sub><sup>d<sub>n-1</sub></sup></i>
     */
    public E getCoefficient(int... d) {
        return terms.get(Monomial.of(d));
    }

    /**
     * Get the coefficient for the monomial <i>x<sub>0</sub><sup>d<sub>0</sub></sup> ... x<sub>n-1</sub><sup>d<sub>n-1</sub></sup></i>
     */
    public E getCoefficient(Monomial d) {
        return terms.get(d);
    }

    public Monomial leadingMonomial() {
        return terms.lastKey();
    }

    public Monomial leadingMonomial(Comparator<Monomial> ordering) {
        if (ordering.equals(terms.comparator())) {
            return terms.lastKey();
        }
        return Collections.max(terms.keySet(), ordering);
    }

    public E leadingCoefficient(Comparator<Monomial> ordering) {
        return getCoefficient(leadingMonomial(ordering));
    }

    public E leadingCoefficient() {
        return terms.get(terms.firstKey());
    }

    public Iterable<Pair<Monomial, E>> coefficients() {
        return () -> terms.keySet().stream().map(d -> new Pair<>(d, getCoefficient(d))).iterator();
    }

    public Iterable<Monomial> monomials() {
        return Collections.unmodifiableSet(terms.keySet());
    }

    public <F> MultivariatePolynomial<F> mapCoefficients(Function<E, F> converter) {
        SortedMap<Monomial, F> newTerms = new TreeMap<>(DEFAULT_ORDERING);
        for (Monomial degree : terms.keySet()) {
            newTerms.put(degree, converter.apply(terms.get(degree)));
        }
        return new MultivariatePolynomial<>(variables, newTerms);
    }

    public static class Builder<S> {

        private final SortedMap<Monomial, S> terms;
        private final Ring<S> ring;
        private final int variables;

        public Builder(int variables, Ring<S> ring) {
            this(variables, ring, DEFAULT_ORDERING);
        }

        public Builder(int variables, Ring<S> ring, MonomialOrdering ordering) {
            this.variables = variables;
            this.ring = ring;
            terms = new TreeMap<>(ordering);
        }

        public Builder<S> add(S c, Monomial degree) {
            assert (degree.variables() == variables);
            if (terms.containsKey(degree)) {
                terms.put(degree, ring.add(terms.get(degree), c));
            } else {
                terms.put(degree, c);
            }
            return this;
        }

        public Builder<S> add(S c, int... degree) {
            return add(c, Monomial.of(degree));
        }

        private void reduce() {
            terms.entrySet().removeIf(e -> ring.equals(e.getValue(), ring.getZero()));

            if (terms.isEmpty()) {
                add(ring.getZero(), new int[variables]);
            }
        }

        public MultivariatePolynomial<S> build() {
            reduce();
            return new MultivariatePolynomial<>(variables, terms);
        }

        public String toString() {
            return terms.toString();
        }

    }

}
