package dk.jonaslindstrom.ruffini.polynomials.elements.recursive;

import dk.jonaslindstrom.ruffini.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

record Monomial<S>(S coefficient, List<Integer> exponents) {

    private static String power(String v, int e) {
        if (e == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(v);
        if (e > 1) {
            stringBuilder.append("^{").append(e).append("}");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        List<String> factors = new ArrayList<>();
        factors.add(coefficient.toString());
        for (int i = 0; i < exponents.size(); i++) {
            factors.add(power(variable(i), exponents.get(i)));
        }
        return StringUtils.productToString(factors);
    }

    private String variable(int i) {
        if (exponents.size() <= 3) {
            return "xyz".substring(i, i + 1);
        }
        return "x_{" + i + "}";
    }
}
