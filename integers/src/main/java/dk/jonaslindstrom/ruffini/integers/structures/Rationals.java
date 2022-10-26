package dk.jonaslindstrom.ruffini.integers.structures;

import dk.jonaslindstrom.ruffini.common.structures.FieldOfFractions;

public class Rationals extends FieldOfFractions<Integer> {

    private static final Rationals instance = new Rationals();

    private Rationals() {
        super(Integers.getInstance());
    }

    public static Rationals getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "\\mathbb{Q}";
    }

}
