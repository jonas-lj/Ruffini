package dk.jonaslindstrom.math.algebra.algorithms.mlp;

import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import org.apache.commons.math3.util.FastMath;

public interface ActivationFunction {

  default Vector<Double> apply(Vector<Double> v) {
    return v.map(this::apply);
  }

  default Vector<Double> applyDerivative(Vector<Double> v) {
    return v.map(this::apply);
  }

  /** Compute the function on input <i>x</i> */
  double apply(double x);

  /** Compute the derivative <i>f'(x)</i>. */
  double applyDerivative(double x);

  /** Compute the derivative <i>f'(x)</i>. The value <i>y</i> is assumed to be equal to <i>f(x)</i>. */
  default double applyDerivative(double x, double y) {
    return applyDerivative(x);
  }

  ActivationFunction relu = new Relu();
  ActivationFunction sigmoid = new Sigmoid();
  ActivationFunction identity = new Identity();
  ActivationFunction tanh = new Tanh();

  class Relu implements ActivationFunction {

    private Relu() {}

    @Override
    public double applyDerivative(double x) {
      return x < 0.0 ? 0.0 : 1.0;
    }

    @Override
    public double apply(double x) {
      return Math.max(x, 0.0);
    }
  }

  class Sigmoid implements ActivationFunction {

    private Sigmoid() {}

    @Override
    public double applyDerivative(double x, double y) {
      return y * (1.0 - y);
    }

    @Override
    public double apply(double x) {
      return 1.0 / (1.0 + FastMath.exp(-x));
    }

    @Override
    public double applyDerivative(double x) {
      double y = apply(x);
      return applyDerivative(x, y);
    }

  }

  class Identity implements ActivationFunction {

    private Identity() {}

    @Override
    public double applyDerivative(double x) {
      return 1.0;
    }

    @Override
    public double apply(double x) {
      return x;
    }

  }

  class Tanh implements ActivationFunction {

    private Tanh() {}

    @Override
    public double apply(double x) {
      return FastMath.tanh(x);
    }

    @Override
    public double applyDerivative(double x) {
      double y = apply(x);
      return applyDerivative(x, y);
    }

    @Override
    public double applyDerivative(double x, double y) {
      return 1 - y * y;
    }


  }

}
