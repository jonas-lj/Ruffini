package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import com.google.common.collect.Streams;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class PolynomialInterpolation<E> implements BiFunction<List<E>, List<E>, Polynomial<E>> {

    private final PolynomialRing<E> polynomialRing;

    public PolynomialInterpolation(Field<E> field) {
        this.polynomialRing = new PolynomialRing<>(field);
    }

    @Override
    public Polynomial<E> apply(List<E> x, List<E> y) {
        int n = x.size();
        if (n != y.size()) {
            throw new IllegalArgumentException("x and y must have the same size");
        }

        Field<E> field = this.polynomialRing.getBaseField();
        Polynomial<E> ľ = new InterpolationTree().apply(x, ArrayUtils.populate(n, i -> field.identity()));
        List<E> evaluations = new BatchPolynomialEvaluation<>(field).apply(ľ, x);
        List<E> l = ArrayUtils.populate(n, i -> field.divide(y.get(i), evaluations.get(i)));
        return new InterpolationTree().apply(x, l);
    }

    private class InterpolationTree implements BiFunction<List<E>, List<E>, Polynomial<E>> {

        @Override
        public Polynomial<E> apply(List<E> x, List<E> y) {
            if (x.size() != y.size()) {
                throw new IllegalArgumentException("x and y must have the same size");
            }
            BinaryTree<Pair<Polynomial<E>, Polynomial<E>>> binaryTree =
                    new BinaryTree<>(ArrayUtils.populate(x.size(), i -> {
                        Polynomial<E> s = Polynomial.of(
                                polynomialRing.getRing().negate(x.get(i)),
                                polynomialRing.getRing().identity());
                        Polynomial<E> l = Polynomial.constant(y.get(i));
                        return new Pair<>(s, l);
                    }), (a, b) -> {
                        Polynomial<E> s = polynomialRing.multiply(a.first, b.first);
                        Polynomial<E> l = polynomialRing.add(
                                polynomialRing.multiply(b.first, a.second),
                                polynomialRing.multiply(a.first, b.second));
                        return new Pair<>(s, l);
                    });
            return binaryTree.getRootLabel().second;
        }
    }
}
