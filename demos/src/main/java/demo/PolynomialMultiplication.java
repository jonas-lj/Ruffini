package demo;

import dk.jonaslindstrom.ruffini.common.algorithms.DiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.algorithms.InverseDiscreteFourierTransform;
import dk.jonaslindstrom.ruffini.common.helpers.PerformanceLoggingField;
import dk.jonaslindstrom.ruffini.common.util.SamplingUtils;
import dk.jonaslindstrom.ruffini.common.vector.Vector;
import dk.jonaslindstrom.ruffini.finitefields.BigPrimeField;
import dk.jonaslindstrom.ruffini.polynomials.elements.Polynomial;
import dk.jonaslindstrom.ruffini.polynomials.structures.PolynomialRing;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PolynomialMultiplication {

    public static void main(String[] arguments) {
        Random random = new Random(1234);
        int n = 18;
        BigInteger modulus = new BigInteger("2523648240000001BA344D80000000086121000000000013A700000000000013", 16);

        // Bn254
        PerformanceLoggingField<BigInteger> field = new PerformanceLoggingField<>(new BigPrimeField(modulus));

        Map<Integer, BigInteger> rootsOfUnity = new HashMap<>();
        rootsOfUnity.put(2, new BigInteger("16798108731015832284940804142231733909889187121439069848933715426072753864722"));
        rootsOfUnity.put(3, new BigInteger("1807136345283977465813277102364620289631804529403213381639"));
        rootsOfUnity.put(6, new BigInteger("1807136345283977465813277102364620289631804529403213381640"));
        rootsOfUnity.put(7, new BigInteger("5058993652618636610753082198083461896929419961886957311101666958794607667612"));
        rootsOfUnity.put(9, new BigInteger("7502905124852926997309799855152694163015671334963250733941146953674568350474"));
        rootsOfUnity.put(14, new BigInteger("15634437159482282363439317643062541599732876391001530504807179722753345987234"));
        rootsOfUnity.put(18, new BigInteger("7723398284040198202184337325741531262426704908898175558891841244448761971785"));

        BigInteger phi = rootsOfUnity.get(n);
        System.out.println("phi = " + phi);

        DiscreteFourierTransform<BigInteger> fdft = new DiscreteFourierTransform<>(field, phi, n);
        InverseDiscreteFourierTransform<BigInteger> ifdft = new InverseDiscreteFourierTransform<>(field, phi, n);
        Vector<BigInteger> pCoefficients = Vector.of(n / 2, i -> SamplingUtils.sample(modulus, random));
        Vector<BigInteger> qCoefficients = Vector.of(n / 2 + 1, i -> SamplingUtils.sample(modulus, random));
        Polynomial<BigInteger> p = new Polynomial<>(pCoefficients, field);
        Polynomial<BigInteger> q = new Polynomial<>(qCoefficients, field);

        PolynomialRing<BigInteger> polynomialRing = new PolynomialRing<>(field);
        field.reset();
        Polynomial<BigInteger> expected = polynomialRing.multiply(p, q);
        System.out.println(field);

        field.reset();
        Vector<BigInteger> pHatCoefficients = fdft.apply(pCoefficients);
        System.out.println(field);
        Vector<BigInteger> qHatCoefficients = fdft.apply(qCoefficients);
        System.out.println(field);
        Vector<BigInteger> sHatCoefficients = pHatCoefficients.coordinateWise(qHatCoefficients, field::multiply);
        System.out.println(field);
        Vector<BigInteger> sCoefficients = ifdft.apply(sHatCoefficients);
        System.out.println(field);

        Polynomial<BigInteger> actual = new Polynomial<>(sCoefficients, field);
        System.out.println(actual.equals(expected));


    }

}
