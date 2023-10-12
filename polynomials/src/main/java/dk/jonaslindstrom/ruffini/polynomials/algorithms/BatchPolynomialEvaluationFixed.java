package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.common.util.ArrayUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Batch polynomial evaluation using a recursive remaindering algorithm. The algorithm was originally
 * presented in A. Borodin, I. Munro (1971), Evaluating polynomials at many points.
 * <p>
 * The complexity is <i>O(M(d) + M(n) log(n))</i> where <i>d</i> is the degree of the polynomial,
 * <i>n</i> is the number of inputs and <i>M(x)</i> is the complexity of multiplying two polynomials of
 * degree <i>x</i>.
 */
public class BatchPolynomialEvaluationFixed<E> implements Function<Polynomial<E>, List<E>> {

    private final PolynomialRingOverRing<E> polynomialRing;
    private final BinaryTree<Polynomial<E>> tree;

    public BatchPolynomialEvaluationFixed(PolynomialRingOverRing<E> polynomialRing, List<E> inputs) {
        this.polynomialRing = polynomialRing;
        this.tree = new BinaryTree<>(inputs.stream().map(leaf -> Polynomial.of(
                polynomialRing.getRing().negate(leaf),
                polynomialRing.getRing().identity())).collect(Collectors.toList()),
                polynomialRing::multiply);

    }

    @Override
    public List<E> apply(Polynomial<E> polynomial) {
        return tree.evaluate(polynomial, this::rem).stream().map(Polynomial::getConstant).collect(Collectors.toList());
    }

    private Polynomial<E> rem(Polynomial<E> u, Polynomial<E> v) {

        int m = u.degree();
        int n = v.degree();

        if (m < n) {
            return u;
        }
        List<E> r = ArrayUtils.populate(m + 1, u::getCoefficient);
        Ring<E> ring = polynomialRing.getRing();

        for (int k = m - n; k >= 0; k--) {
            // The leading coefficient of v is 1
            E q = r.get(n + k);
            for (int j = n+k-1; j >= k; j--) {
                r.set(j, ring.subtract(r.get(j), ring.multiply(q, v.getCoefficient(j - k))));
            }
        }
        return new Polynomial<>(Vector.ofList(r.subList(0, n)), ring);

        //return polynomialRing.divisionWithRemainder(u, v).getSecond();
    }
}
