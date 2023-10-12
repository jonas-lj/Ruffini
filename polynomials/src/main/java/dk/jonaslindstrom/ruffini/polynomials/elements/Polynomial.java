package dk.jonaslindstrom.ruffini.polynomials.elements;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.common.vector.ConstructiveVector;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.algorithms.BatchPolynomialEvaluation;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Polynomial<E> implements BiFunction<E, Ring<E>, E> {

    private final SortedMap<Integer, E> terms;

    public Polynomial(Polynomial<E> p) {
        this(p.terms);
    }

    private Polynomial(SortedMap<Integer, E> terms) {
        this.terms = Collections.unmodifiableSortedMap(terms);
    }

    private Polynomial(Map<Integer, E> terms) {
        this.terms = Collections.unmodifiableSortedMap(new ConcurrentSkipListMap<>(terms));
    }

    public Polynomial(int degree, IntFunction<E> populator) {
        this(IntStream.rangeClosed(0, degree).boxed().collect(Collectors.toList()), populator);
    }

    public Polynomial(Collection<Integer> nonZero, IntFunction<E> populator) {
        this(nonZero.stream().collect(Collectors.toMap(Integer::valueOf, populator::apply)));
    }

    public Polynomial(Vector<E> coefficients, Ring<E> ring) {
        this(IntStream.range(0, coefficients.size()).boxed()
                .filter(i -> !ring.isZero(coefficients.get(i)))
                .collect(Collectors.toMap(i -> i, coefficients::get)));
    }

    public static <T> Polynomial<T> constant(T constant) {
        return Polynomial.monomial(constant, 0);
    }

    public static <T> Polynomial<T> monomial(T coefficient, int degree) {
        return new Polynomial<>(Collections.singletonMap(degree, coefficient));
    }

    /**
     * Construct a new polynomial with the given coefficients. The constant coefficient is first, the
     * linear is second etc.
     */
    @SafeVarargs
    public static <T> Polynomial<T> of(T... coefficients) {
        SortedMap<Integer, T> map = new TreeMap<>();
        for (int i = 0; i < coefficients.length; i++) {
            map.put(i, coefficients[i]);
        }
        return new Polynomial<>(map);
    }

    public void forEach(BiConsumer<Integer, E> consumer) {
        for (Integer i : terms.keySet()) {
            consumer.accept(i, terms.get(i));
        }
    }

    public void forEachInParallel(BiConsumer<Integer, E> consumer) {
        terms.keySet().stream().parallel().forEach(i -> consumer.accept(i, terms.get(i)));
    }

    public <X> Polynomial<X> mapCoefficients(Function<E, X> converter) {
        Map<Integer, X> newTerms = terms.keySet().stream()
                .collect(Collectors.toMap(i -> i, i -> converter.apply(terms.get(i))));
        return new Polynomial<>(newTerms);
    }

    public Polynomial<E> removeTerms(Predicate<E> predicate) {
        Map<Integer, E> newTerms = terms.keySet().stream().filter(p -> !predicate.test(terms.get(p)))
                .collect(Collectors.toMap(i -> i, terms::get));
        return new Polynomial<>(newTerms);
    }

    public Polynomial<E> scale(E scale, Ring<E> ring) {
        return mapCoefficients(e -> ring.multiply(scale, e));
    }

    @Override
    public E apply(E input, Ring<E> ring) {
        Power<E> repeatedSquaring = new Power<>(ring);

        E result = ring.zero();
        E variable = ring.identity();
        int previous = 0;
        for (Integer current : terms.keySet()) {
            variable = ring.multiply(variable, repeatedSquaring.apply(input, current - previous));
            previous = current;
            result = ring.add(result, ring.multiply(terms.get(current), variable));
        }
        return result;
    }

    /**
     * Evaluate this polynomial for all inputs in the given list.
     */
    public List<E> batchApply(List<E> input, PolynomialRingOverRing<E> ring) {
        return new BatchPolynomialEvaluation<>(ring).apply(this, input);
    }

    public int degree() {
        return terms.lastKey();
    }

    /**
     * Get the <i>i</i>'th coefficient or, if it is not present, <code>null</code>.
     */
    public E getCoefficient(int i) {
        return terms.get(i);
    }

    /**
     * Return the coefficients of this polynomial as a vector. The coefficients (of degree less than
     * the degree of the polynomial) that are not present will be replaced by the given zero value.
     */
    public Vector<E> vector(E zero) {
        return new ConstructiveVector<>(degree() + 1, i -> {
            E e = getCoefficient(i);
            if (e != null) {
                return e;
            } else {
                return zero;
            }
        });
    }

    public Polynomial<E> differentiate(Ring<E> ring) {
        Map<Integer, E> newTerms = terms.keySet().stream().filter(i -> i > 0)
                .collect(Collectors.toMap(i -> i - 1, i -> ring.multiply(i, terms.get(i))));
        return new Polynomial<>(newTerms);
    }

    @Override
    public String toString() {
        return toString("x");
    }

    public String toString(String variable, Function<E, String> stringRepresentation) {

        if (terms.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Integer i : terms.keySet()) {
            boolean negative = false;
            if (!i.equals(terms.firstKey())) {

                if (stringRepresentation.apply(terms.get(i)).startsWith("-")) {
                    sb.append(" - ");
                    negative = true;
                } else {
                    sb.append(" + ");
                }
            }

            String c = stringRepresentation.apply(terms.get(i));
            if (negative) {
                c = c.substring(1);
            }

            if (!c.equals("1") | i == 0) {
                sb.append(c);
            }

            if (i == 1) {
                sb.append(variable);
            } else if (i > 1) {
                sb.append(variable).append("^").append(i);
            }

        }
        return sb.toString();
    }

    public String toString(String variable) {
        return toString(variable, E::toString);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial<?> that = (Polynomial<?>) o;
        return Objects.equals(terms, that.terms);
    }

    @Override
    public int hashCode() {
        return terms != null ? terms.hashCode() : 0;
    }

    public E getConstant() {
        return getCoefficient(0);
    }

    public static class Builder<S> {

        private final Ring<S> ring;
        private final SortedMap<Integer, S> terms = new TreeMap<>();

        public Builder(Ring<S> ring) {
            this.ring = ring;
        }

        /**
         * Set the <i>i</i>'th coefficient of the polynomial being built. If this coefficient has been
         * set before, it will be overwritten.
         */
        public Builder<S> set(int i, S a) {
            terms.put(i, a);
            return this;
        }

        /**
         * Add a value to the <i>i</i>'th coefficient of the polynomial being built.
         */
        public synchronized Builder<S> addTo(int i, S a) {
            if (terms.containsKey(i)) {
                set(i, ring.add(terms.get(i), a));
            } else {
                terms.put(i, a);
            }
            return this;
        }

        /**
         * Remove zero terms.
         */
        private Builder<S> reduce() {
            terms.entrySet().removeIf(e -> ring.equals(e.getValue(), ring.zero()));
            if (terms.isEmpty()) {
                set(0, ring.zero());
            }
            return this;
        }

        public Polynomial<S> build() {
            reduce();
            return new Polynomial<>(terms);
        }
    }

}
