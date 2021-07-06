package dk.jonaslindstrom.math.algebra.algorithms.mlp;

import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class LayerFPResult {

  private final Vector<Double> beforeActivation;
  private final Vector<Double> afterActivation;
  private final Vector<Double> input;

  public LayerFPResult(Vector<Double> input, Vector<Double> beforeActivation, Vector<Double> afterActivation) {
    this.input = input;
    this.beforeActivation = beforeActivation;
    this.afterActivation = afterActivation;
  }

  public Vector<Double> getInput() {
    return input;
  }

  public Vector<Double> getBeforeActivation() {
    return beforeActivation;
  }

  public Vector<Double> getAfterActivation() {
    return afterActivation;
  }
}
