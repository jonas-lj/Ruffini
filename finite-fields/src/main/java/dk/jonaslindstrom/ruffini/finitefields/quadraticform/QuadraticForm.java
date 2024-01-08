package dk.jonaslindstrom.ruffini.finitefields.quadraticform;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.abstractions.OrderedSet;
import dk.jonaslindstrom.ruffini.common.algorithms.EuclideanAlgorithm;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class QuadraticForm<E, R extends EuclideanDomain<E> & OrderedSet<E>>
        implements BiFunction<E, E, E> {

    private final E a;
    private final E b;
    private final R ring;
    private final Comparator<E> ordering;
    private final E c;
    private Optional<E> discriminant = Optional.empty();

    public QuadraticForm(R ring, E a, E b, E c) {
        this.ring = ring;
        this.a = a;
        this.b = b;
        this.c = c;
        this.ordering = ring.getOrdering();
    }

    public E getA() {
        return a;
    }

    public E getB() {
        return b;
    }

    public E getC() {
        return c;
    }

    @Override
    public E apply(E x, E y) {
        return ring.add(ring.multiply(a, x, x), ring.multiply(b, x, y), ring.multiply(c, y, y));
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", a, b, c);
    }

    public boolean isPositiveDefinite() {
        return ring.greaterThan(a, ring.zero()) && ring.lessThan(discriminant(), ring.zero());
    }

    public boolean isReduced() {
        if (!isNormal()) {
            return false;
        }

        if (ring.greaterThan(a, c)) {
            return false;
        }

        if (ring.equals(a, c)) {
            return ring.greaterThanOrEqual(b, ring.zero());
        }
        return true;
    }

    private boolean isNormal() {
        return ring.lessThan(ring.negate(a), b) && ring.lessThanOrEqual(b, a);
    }

    private QuadraticForm<E, R> normalize() {
        Pair<E, E> gcd = ring.divide(b, ring.multiply(2, a));
        E q = gcd.first;
        E r = gcd.second;
        if (ring.greaterThan(r, a)) {
            r = ring.subtract(r, ring.multiply(2, a));
            q = ring.add(q, ring.identity());
        }
        return new QuadraticForm<>(ring, a, r, ring.subtract(c, ring.divideExact(ring.multiply(ring.add(b, r), q), 2)));
    }

    public QuadraticForm<E, R> reduce() {
        if (this.isReduced()) {
            return this;
        }

        if (!isNormal()) {
            return normalize().reduce();
        }

        if (ring.greaterThan(a, c)) {
            return new QuadraticForm<>(ring, c, ring.negate(b), a).reduce();
        }

        if (ring.equals(a, c) && ring.lessThan(b, ring.zero())) {
            return new QuadraticForm<>(ring, a, ring.negate(b), c);
        }
        return this;
    }

    public E discriminant() {
        this.discriminant = this.discriminant.or(() -> Optional.of(ring.subtract(ring.multiply(b, b), ring.multiply(4, ring.multiply(a, c)))));
        return this.discriminant.get();
    }

    public QuadraticForm<E, R> compose(QuadraticForm<E, R> other) {
        // Algorithm 5.4.7 in "A Course in Computational Algebraic Number Theory" by Henri Cohen.

        E a1 = this.a;
        E a2 = other.a;
        E b2 = other.b;
        E c2 = other.c;

        if (ordering.compare(a1, a2) > 0) {
            return other.compose(this);
        }

        E D = discriminant();
        if (!ring.equals(D, other.discriminant())) {
            throw new IllegalArgumentException("Discriminants must be equal.");
        }

        EuclideanAlgorithm<E> gcd = new EuclideanAlgorithm<>(ring);

        E s = ring.divideExact(ring.add(this.b, b2), 2);
        E n = ring.subtract(b2, s);

        E d, y1;
        if (ring.divides(a2, a1)) {
            d = a1;
            y1 = ring.zero();
        } else {
            EuclideanAlgorithm.Result<E> duv = gcd.applyExtended(a1, a2);
            d = duv.gcd();
            y1 = duv.y();
        }

        E d1, x2, y2;
        if (ring.isZero(s) || ring.divides(d, s)) {
            x2 = ring.zero();
            y2 = ring.negate(ring.identity());
            d1 = d;
        } else {
            EuclideanAlgorithm.Result<E> duv = gcd.applyExtended(s, d);
            d1 = duv.gcd();
            x2 = duv.x();
            y2 = ring.negate(duv.y());
        }

        E g = gcd.applyExtended(d1, this.c, c2, ring.abs(n, ordering)).gcd();
        E v1 = ring.divideExact(ring.multiply(g, a1), d1);
        E v2 = ring.divideExact(a2, d1);
        E r = ring.subtract(ring.multiply(y1, y2, n), ring.mod(ring.multiply(x2, c2), v1));
        E b3 = ring.add(b2, ring.multiply(2, v2, r));
        E a3 = ring.multiply(v1, v2);
        E c3 = ring.divideExact(ring.subtract(ring.multiply(b3, b3), D), ring.multiply(4, a3));

        return new QuadraticForm<>(ring, a3, b3, c3).reduce();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuadraticForm<?, ?> that = (QuadraticForm<?, ?>) o;

        if (!Objects.equals(a, that.a)) return false;
        if (!Objects.equals(b, that.b)) return false;
        if (!Objects.equals(ring, that.ring)) return false;
        if (!Objects.equals(ordering, that.ordering)) return false;
        if (!Objects.equals(c, that.c)) return false;
        return Objects.equals(discriminant, that.discriminant);
    }
}
