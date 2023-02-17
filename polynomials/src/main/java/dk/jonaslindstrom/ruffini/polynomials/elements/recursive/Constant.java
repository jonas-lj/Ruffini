package dk.jonaslindstrom.ruffini.polynomials.elements.recursive;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * This represents a polynomial with 0 variables, e.g. a constant.
 */
class Constant<S> extends Polynomial<S> {

    private S value;

    private Constant(S value) {
        super(0);
        this.value = value;
    }

    Constant() {
        this(null);
    }

    public void set(S value, List<Integer> indices) {
        if (indices.size() > 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public S get(List<Integer> indices) {
        if (indices.size() > 0) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    @Override
    public Polynomial<S> add(Polynomial<S> other, AdditiveGroup<S> group) {
        if (!(other instanceof Constant<S> otherAsConstant)) {
            throw new IllegalArgumentException();
        }
        return new dk.jonaslindstrom.ruffini.polynomials.elements.recursive.Constant<>(group.add(value, otherAsConstant.value));
    }

    @Override
    public Polynomial<S> multiply(Polynomial<S> other, Ring<S> ring) {
        if (!(other instanceof Constant<S> otherAsConstant)) {
            throw new IllegalArgumentException();
        }
        return new Constant<>(ring.multiply(value, otherAsConstant.value));
    }

    @Override
    protected Stream<Pair<S, LinkedList<Integer>>> getTerms() {
        return Stream.of(new Pair<>(value, new LinkedList<>()));
    }

    @Override
    public S apply(List<S> x, Ring<S> ring) {
        return this.value;
    }

    public Pair<Polynomial<S>, Polynomial<S>> divide(Polynomial<S> other, Ring<S> ring) {
        if (!(other instanceof Constant<S> otherAsConstant)) {
            throw new IllegalArgumentException();
        }

        S result;
        if (ring instanceof Field<S> field) {
            result = field.divide(this.value, otherAsConstant.value);
        } else if (ring.isIdentity(otherAsConstant.value)) {
            result = this.value;
        } else {
            throw new IllegalArgumentException();
        }
        return new Pair<>(new Constant<>(result), new Constant<>(ring.getZero()));
    }

    protected Polynomial<S> replace(UnaryOperator<S> operation) {
        return new Constant<>(operation.apply(this.value));
    }

    @Override
    public boolean isZero(AdditiveGroup<S> group) {
        return group.isZero(this.value);
    }

    public String toString() {
        return value.toString();
    }

    protected Polynomial<S> reduce(AdditiveGroup<S> group) {
        return this;
    }
}