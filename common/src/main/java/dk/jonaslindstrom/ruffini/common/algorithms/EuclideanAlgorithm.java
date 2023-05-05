package dk.jonaslindstrom.ruffini.common.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.EuclideanDomain;
import dk.jonaslindstrom.ruffini.common.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EuclideanAlgorithm<E> {

    private final EuclideanDomain<E> ring;

    public EuclideanAlgorithm(EuclideanDomain<E> domain) {
        this.ring = domain;
    }

    public E apply(E a, E b) {
        if (ring.isZero(b)) {
            return a;
        }
        return apply(b, ring.mod(a, b));
    }

    public E apply(List<E> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("Empty input list");
        }
        
        if (inputs.size() == 1) {
            return inputs.get(0);
        }
        return apply(inputs.get(0), apply(inputs.subList(1, inputs.size())));
    }

    public E apply(E... inputs) {
        return apply(List.of(inputs));
    }

    /**
     * Calculate the greatest common divisor <i>d</i> of <i>a</i> and <i>b</i>, and the coefficients
     * <i>x,y</i> of the Bezout identity <i>ax + by = d</i> .
     *
     * @return The triple <i>(d, x, y)</i>.
     */
    public Result<E> applyExtended(E a, E b) {
        E s_1 = ring.zero();
        E s_0 = ring.identity();
        E t_1 = ring.identity();
        E t_0 = ring.zero();
        E r_1 = b;
        E r_0 = a;

        while (!ring.equals(r_1, ring.zero())) {
            Pair<E, E> division = ring.divide(r_0, r_1);
            E q = division.getFirst();

            r_0 = r_1;
            r_1 = division.getSecond();

            E tmpS = s_1;
            s_1 = ring.add(s_0, ring.negate(ring.multiply(q, tmpS)));
            s_0 = tmpS;

            E tmpT = t_1;
            t_1 = ring.add(t_0, ring.negate(ring.multiply(q, tmpT)));
            t_0 = tmpT;
        }
        return new Result<>(r_0, s_0, t_0);
    }

    public ExtendedResult<E> applyExtended(List<E> inputs) {
        if (inputs.size() == 1) {
            return new ExtendedResult<>(inputs.get(0), List.of(this.ring.identity()));
        }
        ExtendedResult<E> recursion = applyExtended(inputs.subList(1, inputs.size()));
        Result<E> result = applyExtended(inputs.get(0), recursion.gcd);
        LinkedList<E> bezoutCoefficients = recursion.bezout.stream().map(bPrime -> ring.multiply(bPrime, result.y())).collect(Collectors.toCollection(LinkedList::new));
        bezoutCoefficients.addFirst(result.x());
        return new ExtendedResult<>(result.gcd, bezoutCoefficients);
    }

    public ExtendedResult<E> applyExtended(E... inputs) {
        return applyExtended(List.of(inputs));
    }

    public record Result<E>(E gcd, E x, E y) {

    }

    public record ExtendedResult<E>(E gcd, List<E> bezout) {

    }

}
