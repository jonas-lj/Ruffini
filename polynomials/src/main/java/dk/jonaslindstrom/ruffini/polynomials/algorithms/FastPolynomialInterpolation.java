package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FastPolynomialInterpolation<E> implements Function<List<Pair<E, E>>, Polynomial<E>> {

    private final PolynomialRing<E> polynomialRing;

    public FastPolynomialInterpolation(Field<E> field) {
        this.polynomialRing = new PolynomialRing<>(field);
    }

    @Override
    public Polynomial<E> apply(List<Pair<E, E>> points) {
        List<Pair<E, E>> normalizedPoints = points.stream().map(point -> Pair.of(point.first, polynomialRing.getRing().identity())).toList();
        Polynomial<E> l_prime = new InterpolationTree<>(this.polynomialRing).apply(normalizedPoints);
        List<E> ξ = points.stream().map(Pair::getFirst).toList();
        List<E> evaluations = new BatchPolynomialEvaluation<>(this.polynomialRing.getRing()).apply(l_prime, ξ);
        List<E> lambda = new ArrayList<>(evaluations.stream().map(this.polynomialRing.getBaseField()::invert).toList());
        for (int i = 0; i < ξ.size(); i++) {
            lambda.set(i, polynomialRing.getRing().multiply(lambda.get(i), points.get(i).second));
        }
        List<Pair<E, E>> computedPoints = IntStream.range(0, ξ.size()).boxed().map(i -> Pair.of(ξ.get(i), lambda.get(i))).toList();
        return new InterpolationTree<>(this.polynomialRing).apply(computedPoints);
    }

    public static class InterpolationTree<E> implements Function<List<Pair<E, E>>, Polynomial<E>> {

        private final PolynomialRing<E> polynomialRing;

        public InterpolationTree(PolynomialRing<E> polynomialRing) {
            this.polynomialRing = polynomialRing;
        }

        @Override
        public Polynomial<E> apply(List<Pair<E, E>> points) {
            BinaryTree<Pair<Polynomial<E>, Polynomial<E>>> binaryTree =
                    new BinaryTree<>(points.stream().map(point -> {
                        Polynomial<E> s = Polynomial.of(
                                polynomialRing.getRing().negate(point.first),
                                polynomialRing.getRing().identity());
                        Polynomial<E> l = Polynomial.constant(point.second);
                        return new Pair<>(s, l);
                    }).collect(Collectors.toList()), (a, b) -> {
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
