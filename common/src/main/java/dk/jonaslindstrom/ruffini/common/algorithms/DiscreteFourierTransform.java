package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * Compute the Discrete Fourier Transform over a ring.
 *
 * @param <E> Element type.
 */
public class DiscreteFourierTransform<E> implements UnaryOperator<Vector<E>> {

    private final Ring<E> ring;
    private final E a;
    private final int n;
    private final FDFT<E> fdft;

    public DiscreteFourierTransform(Ring<E> ring, E nThPrincipalRootOfUnity, int n) {
        this.ring = ring;
        this.a = nThPrincipalRootOfUnity;
        this.n = n;
        this.fdft = new FDFT<>(ring, a, n);
    }

    @Override
    public Vector<E> apply(Vector<E> x) {
        return fdft.apply(x.pad(n, ring.zero()));
    }

    private static class DFT<F> implements UnaryOperator<Vector<F>> {

        private final Ring<F> ring;
        private final int n;
        private final ArrayList<F> powers;

        public DFT(Ring<F> ring, F a, int n) {
            this.ring = ring;
            this.n = n;
            Power<F> power = new Power<>(ring);
            this.powers = ArrayUtils.populate(n*n, j -> power.apply(a, j));
        }

        @Override
        public Vector<F> apply(Vector<F> v) {
            assert (v.size() == n);
            Sum<F> sum = new Sum<>(ring);

            return Vector.of(n,
                    k -> sum.apply(j -> ring.multiply(v.get(j), powers.get(j * k)), n));
        }
    }

    public class FDFT<F> implements UnaryOperator<Vector<F>> {

        private final Ring<F> ring;
        private final int n;
        private final ArrayList<F> powers;
        private final UnaryOperator<Vector<F>> sub;

        public FDFT(Ring<F> ring, F a, int n) {
            this.ring = ring;
            this.n = n;
            this.powers = ArrayUtils.populate(n, i -> new Power<>(ring).apply(a, i));
            if (n % 2 != 0) {
                if (n == 1) {
                    this.sub = null;
                } else {
                    this.sub = new DFT<>(ring, a, n);
                }
            } else {
                this.sub = new FDFT<>(ring, ring.multiply(a, a), n / 2);
            }
        }

        @Override
        public Vector<F> apply(Vector<F> x) {
            assert (x.size() == n);

            if (n % 2 != 0) {
                if (n == 1) {
                    return Vector.of(x.get(0));
                } else {
                    return sub.apply(x);
                }
            }

            Vector<F> even = sub.apply(Vector.view(n / 2, i -> x.get(2 * i)));
            Vector<F> odd = sub.apply(Vector.view(n / 2, i -> x.get(2 * i + 1)));

            ArrayList<F> Y = new ArrayList<>(Collections.nCopies(n, null));

            IntStream.range(0, n / 2).forEach(k -> {
                F ak = ring.multiply(powers.get(k), odd.get(k));
                Y.set(k, ring.add(even.get(k), ak));
                Y.set(k + n / 2, ring.subtract(even.get(k), ak));
            });

            return Vector.ofList(Y);
        }

    }

}
