package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.common.abstractions.Ring;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Batch polynomial evaluation using a recursive remaindering algorithm. The algorithm was originally
 * presented in A. Borodin, I. Munro (1971), Evaluating polynomials at many points.
 * <p>
 * The complexity is <i>O(M(d) + M(n) log(n))</i> where <i>d</i> is the degree of the polynomial,
 * <i>n</i> is the number of inputs and <i>M(x)</i> is the complexity of multiplying two polynomials of
 * degree <i>x</i>.
 */
public class BatchPolynomialEvaluation<E> implements BiFunction<Polynomial<E>, List<E>, List<E>> {

    private final PolynomialRingOverRing<E> polynomialRing;

    public BatchPolynomialEvaluation(PolynomialRingOverRing<E> polynomialRing) {
        this.polynomialRing = polynomialRing;
    }

    @Override
    public List<E> apply(Polynomial<E> polynomial, List<E> inputs) {
        BinaryTree<Polynomial<E>> binaryTree = new BinaryTree<>(inputs.stream().map(leaf -> Polynomial.of(
                        polynomialRing.getRing().negate(leaf),
                        polynomialRing.getRing().identity())).collect(Collectors.toList()),
                polynomialRing::multiply);
        return binaryTree.evaluate(polynomial, (a,b) -> polynomialRing.divisionWithRemainder(a, b).getSecond()).stream().map(Polynomial::getConstant).collect(Collectors.toList());
    }
}
