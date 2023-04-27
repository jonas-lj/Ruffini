package dk.jonaslindstrom.ruffini.elliptic.structures;

import dk.jonaslindstrom.ruffini.common.abstractions.AdditiveGroup;
import dk.jonaslindstrom.ruffini.common.abstractions.Field;
import dk.jonaslindstrom.ruffini.common.algorithms.Multiply;
import dk.jonaslindstrom.ruffini.common.algorithms.Power;
import dk.jonaslindstrom.ruffini.elliptic.elements.ProjectivePoint;

public class ShortWeierstrassCurveProjective<E> implements AdditiveGroup<ProjectivePoint<E>> {

    private final E a, b;
    private final ProjectivePoint<E> O;
    private final Field<E> field;

    /**
     * Curve on Weierstrass form. Field should have characteristics not equal to 2 or 3.
     */
    public ShortWeierstrassCurveProjective(Field<E> field, E a, E b) {
        this.field = field;
        this.a = a;
        this.b = b;
        assert (!field.equals(discriminant(), field.zero()));
        this.O = ProjectivePoint.pointAtInfinity(field);
    }

    public Field<E> getField() {
        return field;
    }

    public E discriminant() {
        Multiply<E> multiply = new Multiply<>(field);
        Power<E> power = new Power<>(field);

        return field.negate(multiply.apply(16,
                field.add(
                        multiply.apply(4, power.apply(a, 3)),
                        multiply.apply(27, field.multiply(b, b)))));
    }

    public E getA() {
        return a;
    }

    public E getB() {
        return b;
    }

    @Override
    public ProjectivePoint<E> negate(ProjectivePoint<E> p) {
        return new ProjectivePoint<>(p.X(), field.negate(p.Y()), p.Z());
    }

    @Override
    public ProjectivePoint<E> add(ProjectivePoint<E> p, ProjectivePoint<E> q) {

        // add-2007-bl by Bernstein and Lange
        //compute U1 = X1 Z2
        E u1 = field.multiply(p.X(), q.Z());

        //compute U2 = X2 Z1
        E u2 = field.multiply(q.X(), p.Z());

        //compute S1 = Y1 Z2
        E s1 = field.multiply(p.Y(), q.Z());

        //compute S2 = Y2 Z1
        E s2 = field.multiply(q.Y(), p.Z());

        //compute ZZ = Z1 Z2
        E zz = field.multiply(p.Z(), q.Z());

        //compute T = U1+U2
        E t = field.add(u1, u2);

        //compute TT = T^2
        E tt = field.multiply(t, t);

        //compute M = S1+S2
        E m = field.add(s1, s2);

        //compute R = TT-U1 U2+a ZZ^2
        E r = field.add(field.subtract(tt, field.multiply(u1, u2)), field.multiply(a, zz, zz));

        //compute F = ZZ M
        E f = field.multiply(zz, m);

        //compute L = M F
        E l = field.multiply(m, f);

        //compute LL = L^2
        E ll = field.multiply(l, l);

        //compute G = (T+L)^2-TT-LL
        E tpl = field.add(t, l);
        E g = field.subtract(field.multiply(tpl, tpl), field.add(tt, ll));

        //compute W = 2 R^2-G
        E rr = field.multiply(r, r);
        E w = field.subtract(field.doubling(rr), g);

        //compute X3 = 2 F W
        E fw = field.multiply(f, w);
        E x = field.doubling(fw);

        //compute Y3 = R(G-2 W)-2 LL
        E y = field.subtract(field.multiply(r, field.subtract(g, field.doubling(w))), field.doubling(ll));

        //compute Z3 = 4 F F^2
        E fff = field.multiply(f, f, f);
        E z = field.add(fff, fff, fff, fff);

        return new ProjectivePoint<>(x, y, z);
    }

    @Override
    public ProjectivePoint<E> zero() {
        return O;
    }

    @Override
    public String toString(ProjectivePoint<E> p) {
        return p.toString();
    }

    @Override
    public boolean equals(ProjectivePoint<E> p, ProjectivePoint<E> q) {
        return field.equals(field.multiply(p.X(), q.Z()), field.multiply(q.X(), p.Z())) && field.equals(field.multiply(p.Y(), q.Z()), field.multiply(q.Y(), p.Z()));
    }

    @Override
    public ProjectivePoint<E> doubling(ProjectivePoint<E> p) {
        //compute XX = X1^2
        E xx = field.multiply(p.X(), p.X());

        //compute ZZ = Z1^2
        E zz = field.multiply(p.Z(), p.Z());

        //compute w = a ZZ+3 XX
        E w = field.add(field.multiply(a, zz), field.add(xx, xx, xx));

        //compute s = 2 Y1 Z1
        E yz = field.multiply(p.Y(), p.Z());
        E s = field.doubling(yz);

        //compute ss = s^2
        E ss = field.multiply(s, s);

        //compute sss = s ss
        E sss = field.multiply(s, ss);

        //compute R = Y1 s
        E r = field.multiply(p.Y(), s);

        //compute RR = R^2
        E rr = field.multiply(r, r);

        //compute B = (X1+R)^2-XX-RR
        E xpr = field.add(p.X(), r);
        E b = field.subtract(field.multiply(xpr, xpr), field.add(xx, rr));

        //compute h = w^2-2 B
        E h = field.subtract(field.multiply(w, w), field.doubling(b));

        //compute X3 = h s
        E x = field.multiply(h, s);

        //compute Y3 = w(B-h)-2 RR
        E y = field.subtract(field.multiply(w, field.subtract(b, h)), field.doubling(rr));

        //compute Z3 = sss
        E z = sss;

        return new ProjectivePoint<>(x, y, z);
    }

    public String toString() {
        return "E: y^2 = x^3 + " + a + " x + " + b;
    }

}
