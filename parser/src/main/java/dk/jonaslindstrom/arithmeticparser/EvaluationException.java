package dk.jonaslindstrom.arithmeticparser;

import java.io.Serial;

public class EvaluationException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public EvaluationException(String string) {
        super(string);
    }
}
