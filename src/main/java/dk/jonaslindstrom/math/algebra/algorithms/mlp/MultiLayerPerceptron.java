package dk.jonaslindstrom.math.algebra.algorithms.mlp;

import com.google.common.collect.Streams;
import dk.jonaslindstrom.math.algebra.algorithms.DotProduct;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.BPResult;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.FPResult;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerBPResult;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerFPResult;
import dk.jonaslindstrom.math.algebra.concretisations.RealCoordinateSpace;
import dk.jonaslindstrom.math.algebra.concretisations.RealNumbers;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;
import dk.jonaslindstrom.math.util.ArgMax;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiLayerPerceptron {

  private final List<Layer> layers;

  public MultiLayerPerceptron(List<Layer> layers) {
    this.layers = layers;
  }

  public List<Layer> getLayers() {
    return layers;
  }

  public FPResult forwardpropagate(Vector<Double> in) {
    FPResult result = new FPResult();
    for (Layer layer : layers) {
      LayerFPResult fw = layer.forwardpropagate(in);
      result.add(fw);
      in = fw.getAfterActivation();
    }
    return result;
  }

  public BPResult backpropagate(FPResult outputs, Vector<Double> error) {
    BPResult result = new BPResult();
    LayerBPResult previous = new LayerBPResult(error, null);
    for (int l = layers.size() - 1; l >= 0; l--) {
      previous = layers.get(l)
          .backpropagate(previous, outputs.get(l));
      result.add(previous);
    }
    return result;
  }

  public MultiLayerPerceptron fit(List<Vector<Double>> data, List<Vector<Double>> labels, int batchSize,
      int epochs, double alpha,
      BiConsumer<Integer, Double> errorRateConsumer) {

    RealCoordinateSpace Rn = new RealCoordinateSpace(labels.get(0).size());
    DotProduct<Double> dotProduct = new DotProduct<>(RealNumbers.getInstance());

    MultiLayerPerceptron currentNetwork = this;

    for (int epoch = 0; epoch < epochs; epoch++) {
      double error = 0.0;
      for (int i = 0; i < data.size(); i += batchSize) {

        int thisBatchSize = Math.min(batchSize, data.size() - i);

        MultiLayerPerceptron finalCurrentNetwork = currentNetwork;

        // Perform forward propagation for all samples in batch
        List<FPResult> outputs = IntStream
            .range(i, i + thisBatchSize).parallel().mapToObj(data::get).map(finalCurrentNetwork::forwardpropagate).collect(
                Collectors.toList());

        // Retrieve list of expected outputs
        List<Vector<Double>> expected = IntStream.range(i, i + thisBatchSize).parallel().mapToObj(labels::get)
            .collect(
                Collectors.toList());

        // Compute errors for all samples
        List<Vector<Double>> errors = Streams
            .zip(outputs.stream().map(FPResult::getOutput), expected.stream(),
                (o, e) -> Rn.subtract(e, o)).collect(
                Collectors.toList());

        // Perform bach propagation for all samples in batch
        List<BPResult> bpResult = Streams
            .zip(outputs.stream().parallel(), errors.stream().parallel(),
                finalCurrentNetwork::backpropagate).collect(Collectors.toList());

        // Update layers based on the back propagation outputs
        List<Layer> newLayers = new ArrayList<>(currentNetwork.layers.size());
        for (int l = 0; l < currentNetwork.layers.size(); l++) {
          Layer current = currentNetwork.layers.get(l);
          for (int j = 0; j < thisBatchSize; j++) {
            current = current.update(outputs.get(j).get(l), bpResult.get(j)
                .get(l), alpha);
          }
          newLayers.add(current);
        }
        currentNetwork = new MultiLayerPerceptron(newLayers);

        error += errors.stream().mapToDouble(e -> dotProduct.apply(e, e)).sum();
      }
      errorRateConsumer.accept(epoch, error);
    }
    return currentNetwork;
  }


  public int predict(Vector<Double> input) {
    for (Layer layer : layers) {
      input = layer.forwardpropagate(input).getAfterActivation();
    }
    return new ArgMax<Double>().applyAsInt(input.asList());
  }

}
