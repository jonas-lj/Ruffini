package demo;

import dk.jonaslindstrom.ruffini.polynomials.elements.recursive.Polynomial;

public class MultivariatePolynomials {

    public static void main(String[] arguments) {

        Polynomial<Integer> p = Polynomial.create(2);

        p.set(1, 3, 0);
        p.set(2, 2, 1);
        p.set(-1, 0, 3);

        System.out.println(p);

        Polynomial<Integer> q = Polynomial.create(2);

        q.set(1, 1, 0);
        q.set(1, 0, 1);

        System.out.println(q);
    }

}
