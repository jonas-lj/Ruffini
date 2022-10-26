package dk.jonaslindstrom.ruffini.reals.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.VectorSpace;
import dk.jonaslindstrom.ruffini.common.algorithms.Sum;
import dk.jonaslindstrom.ruffini.common.util.Pair;
import dk.jonaslindstrom.ruffini.reals.structures.RealNumbers;

import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Numerical integration using the Runge-Kutta method.
 */
public class RungeKutta<V> {

    private final BiFunction<Double, V, V> f;
    private final VectorSpace<V, Double, RealNumbers> V;
    private final Sum<V> sum;
    private double t;
    private V y;

    public RungeKutta(BiFunction<Double, V, V> f, double t0,
                      V y0, VectorSpace<V, Double, RealNumbers> V) {
        this.f = f;
        this.t = t0;
        this.y = y0;
        this.V = V;
        this.sum = new Sum<>(V);
    }

    /**
     * Iterate the integrator by <i>dt</i> time units and return the resulting time and point
     */
    public Pair<Double, V> step(double dt) {
        V k1 = f.apply(t, y);
        V k2 = f.apply(t + dt / 2, V.add(y, V.scale(dt / 2, k1)));
        V k3 = f.apply(t + dt / 2, V.add(y, V.scale(dt / 2, k2)));
        V k4 = f.apply(t + dt, V.add(y, V.scale(dt, k3)));

        t = t + dt;
        y = V.add(y, V.scale(dt / 6, sum.apply(k1, V.scale(2.0, k2), V.scale(2.0, k3), k4)));
        return new Pair<>(t, y);
    }

    /**
     * Get a stream of points with the given step size.
     */
    public Stream<Pair<Double, V>> stream(double dt) {
        return Stream.iterate(new Pair<>(t, y), v -> step(dt));
    }

}
