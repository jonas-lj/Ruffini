package dk.jonaslindstrom.ruffini.common.helpers;

import dk.jonaslindstrom.ruffini.common.abstractions.Field;

/**
 * Wrapper for the ring class which logs the number of operations performed in this ring.
 */
public class PerformanceLoggingField<E> extends PerformanceLoggingRing<E> implements Field<E> {
    private final Field<E> field;
    private int inversions;

    public PerformanceLoggingField(Field<E> field) {
        super(field);
        this.field = field;
    }

    public E invert(E a) {
        if (isIdentity(a)) {
            return a;
        }
        synchronized (this) {
            inversions++;
        }
        return field.invert(a);
    }

    public int getInversions() {
        return inversions;
    }


    public String toString() {
        return super.toString() + "\ninv   = " + inversions;
    }

    public void reset() {
        super.reset();
        inversions = 0;
    }
}
