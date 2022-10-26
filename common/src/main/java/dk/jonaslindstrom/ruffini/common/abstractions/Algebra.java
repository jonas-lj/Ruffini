package dk.jonaslindstrom.ruffini.common.abstractions;

public interface Algebra<V, S, R extends Ring<S>> extends Module<V, S, R> {

    /**
     * Return the result of the multiplication of <i>u</i> and <i>v</i></i> in this algebra.
     */
    V multiply(V v, V u);

}
