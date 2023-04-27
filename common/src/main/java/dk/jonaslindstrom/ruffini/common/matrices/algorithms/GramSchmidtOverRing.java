package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.algorithms.DotProduct;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.common.vector.ConcreteVector;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class GramSchmidtOverRing<E> implements UnaryOperator<List<Vector<E>>> {

    private final EuclideanDomain<E> ring;
    private final EuclideanAlgorithm<E> euclideanAlgorithm;

    public GramSchmidtOverRing(EuclideanDomain<E> ring) {
        this.ring = ring;
        this.euclideanAlgorithm = new EuclideanAlgorithm<>(ring);
    }

    private Vector<E> scale(E s, Vector<E> v) {
        return v.map(e -> ring.multiply(s, e));
    }

    private Vector<E> sub(Vector<E> v, Vector<E> u) {
        return new ConcreteVector<>(v.size(), i -> ring.add(v.get(i), ring.negate(u.get(i))));
    }

    private E gcd(E a, E b) {
        return euclideanAlgorithm.gcd(a, b).gcd();
    }

    private Vector<E> normalize(Vector<E> v) {
        E g = v.stream().reduce(this::gcd).get();
        return v.map(e -> ring.divide(e, g).first);
    }

    @Override
    public List<Vector<E>> apply(List<Vector<E>> t) {
        LinkedList<Pair<Vector<E>, E>> U = new LinkedList<>();
        LinkedList<Vector<E>> V = new LinkedList<>(t);

        DotProduct<E> dot = new DotProduct<>(ring);

        E product = ring.identity();
        while (!V.isEmpty()) {
            Vector<E> v = V.poll();
            Vector<E> w = scale(product, v);

            for (Pair<Vector<E>, E> pair : U) {
                Vector<E> u = pair.first;
                Vector<E> p = scale(dot.apply(v, u), u);
                p = scale(pair.second, p);
                w = sub(w, p);
            }

            w = normalize(w);

            E i = dot.apply(w, w);
            for (Pair<Vector<E>, E> pair : U) {
                pair.second = ring.multiply(pair.second, i);
            }

            U.add(new Pair<>(w, product));
            product = ring.multiply(product, i);
        }

        return U.stream().map(u -> u.first).collect(Collectors.toList());
    }

}
