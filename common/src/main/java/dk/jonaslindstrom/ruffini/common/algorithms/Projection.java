package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.abstractions.InnerProductSpace;
import dk.jonaslindstrom.ruffini.common.abstractions.VectorSpace;

import java.util.function.BiFunction;

/**
 * Compute the projection of a vector <i>v</i> onto another vector <i>u</i> in the given vector
 * space.
 */
public class Projection<V, S, F extends Field<S>> implements BiFunction<V, V, V> {

    private final VectorSpace<V, S, F> vectorSpace;
    private final BiFunction<V, V, S> innerProduct;

    public Projection(InnerProductSpace<V, S, F> innerProductSpace) {
        this(innerProductSpace, innerProductSpace::innerProduct);
    }

    public Projection(VectorSpace<V, S, F> vectorSpace,
                      BiFunction<V, V, S> innerProduct) {
        this.vectorSpace = vectorSpace;
        this.innerProduct = innerProduct;
    }

    @Override
    public V apply(V v, V u) {
        S s = vectorSpace.getScalars().divide(innerProduct.apply(u, v), innerProduct.apply(u, u));
        return vectorSpace.scale(s, u);
    }

}
