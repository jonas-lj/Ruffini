package dk.jonaslindstrom.math.algebra.algorithms.mlp;

import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerBPResult;
import dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos.LayerFPResult;
import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public interface Layer {

  /** Returns a pair where the first is the output before activation and the second is the output after activation */
  LayerFPResult forwardpropagate(Vector<Double> input);

  /** Returns a pair where the first is the backpropagated error = weightsT * delta and the second is delta. */
  LayerBPResult backpropagate(LayerBPResult previous,
      LayerFPResult output);

  /** Compute a new layer from this one based on the given delta, input and learning rate. */
  Layer update(LayerFPResult fpResult, LayerBPResult bpResult, double alpha);

}
