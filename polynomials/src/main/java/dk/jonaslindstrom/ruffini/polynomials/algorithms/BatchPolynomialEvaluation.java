package dk.jonaslindstrom.ruffini.polynomials.algorithms;

import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRingOverRing;

import java.util.List;
import java.util.function.Function;

/**
 * Batch polynomial evaluation using the Moenck–Borodin algorithm. The algorithm was originally
 * presented in "Evaluating polynomials at many points" by A. Borodin and I. Munro (1971),
 * <p>
 * The complexity is <i>O(M(d) + M(n) log(n))</i> where <i>d</i> is the degree of the polynomial,
 * <i>n</i> is the number of inputs and <i>M(x)</i> is the complexity of multiplying two polynomials of
 * degree <i>x</i>.
 */
public class BatchPolynomialEvaluation<E> implements Function<Polynomial<E>, List<E>> {

    private final BinaryTree.SubproductTree<E> tree;

    public BatchPolynomialEvaluation(PolynomialRingOverRing<E> polynomialRing, List<E> inputs) {
        this.tree = new BinaryTree.SubproductTree<>(inputs, polynomialRing);
    }

    @Override
    public List<E> apply(Polynomial<E> polynomial) {
        return tree.evaluate(polynomial);
    }

}
