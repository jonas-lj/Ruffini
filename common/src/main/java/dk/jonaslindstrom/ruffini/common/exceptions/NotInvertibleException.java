package dk.jonaslindstrom.ruffini.common.exceptions;

public class NotInvertibleException extends RuntimeException {

    private final Object element;

    public NotInvertibleException(Object element) {
        super("Element " + element + " is not invertible");
        this.element = element;
    }

    /**
     * The element that turned out not to be invertible.
     */
    public Object getElement() {
        return element;
    }

}
