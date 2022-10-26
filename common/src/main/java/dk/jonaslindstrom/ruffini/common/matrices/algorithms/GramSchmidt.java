package dk.jonaslindstrom.ruffini.common.matrices.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.InnerProductSpace;
import dk.jonaslindstrom.ruffini.common.abstractions.VectorSpace;
import dk.jonaslindstrom.ruffini.common.algorithms.Projection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GramSchmidt<V, S, F extends Field<S>> implements Function<List<V>, List<V>> {

    private final VectorSpace<V, S, F> vectorSpace;
    private final BiFunction<V, V, S> innerProduct;

    public GramSchmidt(InnerProductSpace<V, S, F> S) {
        this(S, S::innerProduct);
    }

    public GramSchmidt(VectorSpace<V, S, F> V,
                       BiFunction<V, V, S> innerProduct) {
        this.vectorSpace = V;
        this.innerProduct = innerProduct;
    }

    @Override
    public List<V> apply(List<V> vectors) {
        List<V> U = new ArrayList<>();

        Projection<V, S, F> proj = new Projection<>(vectorSpace, innerProduct);

        for (V v : vectors) {
            for (V u : U) {
                V p = proj.apply(v, u);
                v = vectorSpace.subtract(v, p);
            }
            U.add(v);
        }
        return U;
    }

}
