package dk.jonaslindstrom.ruffini.elliptic.elements;

/**
 * Instances of this class represents a point on an <a href="https://en.wikipedia.org/wiki/Edwards_curve">Edwards Curve</a>
 * over a field with elements of type <code>E</code>. Note that <i>O = (0,1)</i> on Edwards Curves.
 *
 * @param <E> The element type for the underlying field.
 */
public record EdwardsPoint<E>(E x, E y) {

    public String toString() {
        return String.format("(%s, %s)", x.toString(), y.toString());
    }
}
