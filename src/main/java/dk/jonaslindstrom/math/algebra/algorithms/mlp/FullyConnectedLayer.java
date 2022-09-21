package dk.jonaslindstrom.math.algebra.algorithms.mlp;

import dk.jonaslindstrom.math.algebra.abstractions.Field;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerBPResult;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerFPResult;
import dk.jonaslindstrom.math.algebra.concretisations.RealNumbers;
import dk.jonaslindstrom.math.algebra.elements.matrix.Matrix;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.Pair;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FullyConnectedLayer implements Layer {

  private final Matrix<Double> weights;
  private final Vector<Double> bias;
  private final ActivationFunction activationFunction;

  private static final Field<Double> R = RealNumbers.getInstance();

  public FullyConnectedLayer(Matrix<Double> weights, Vector<Double> bias, ActivationFunction activationFunction) {
    this.weights = weights;
    this.bias = bias;
    this.activationFunction = activationFunction;
  }

  public FullyConnectedLayer(double[][] in, double[] bias, ActivationFunction activationFunction) {
    this(Matrix.of(in.length, in[0].length, (i,j) -> in[i][j], true),
        Vector.of(bias.length, i -> bias[i], true), activationFunction);
  }

  public FullyConnectedLayer(int in, int out, ActivationFunction activationFunction, Supplier<Double> weightInitializer, Supplier<Double> biasInitializer) {
    this(Matrix.of(out, in, (i,j) -> weightInitializer.get(), true),
        Vector.of(out, i -> biasInitializer.get(), true), activationFunction);
  }

  public FullyConnectedLayer(int in, int out, ActivationFunction activationFunction, Random random) {
    // Xavier initialization
    this(in, out, activationFunction, () -> random.nextGaussian() / out, () -> random.nextGaussian() / out);
  }

  @Override
  public LayerFPResult forwardpropagate(Vector<Double> in) {
    Vector<Double> output = weights.apply(in, R);
    output = Vector.op(output, bias, R::add);
    return new LayerFPResult(in, output, output.map(activationFunction::apply));
  }

  @Override
  public LayerBPResult backpropagate(
      LayerBPResult error,
      LayerFPResult output) {
    Vector<Double> delta = Vector.op(error.getError(), Vector.op(output.getBeforeActivation(), output.getAfterActivation(),
        Pair::new).map(p -> activationFunction.applyDerivative(p.first, p.second)), (a,b) -> a*b);
    Vector<Double> newError = weights.transpose().apply(delta, R);
    return new LayerBPResult(newError, delta);
  }

  @Override
  public Layer update(LayerFPResult fpResult, LayerBPResult bpResult, double alpha) {
    Matrix<Double> newWeights = Matrix.of(weights.getHeight(), weights.getWidth(), (i,j) ->
        weights.get(i,j) + bpResult.getDelta().get(i) * fpResult.getInput().get(j) * alpha);
    Vector<Double> newBias = Vector.of(bias.size(), i -> bias.get(i) + bpResult.getDelta().get(i) * alpha);
    return new FullyConnectedLayer(newWeights, newBias, activationFunction);
  }

  @Override
  public List<Matrix<Double>> getWeights() {
    return List.of(weights, bias.asRow());
  }

  public String toString() {
    return "Weights: \n" + weights + "\nBias:\n" + bias + "\n";
  }

}
