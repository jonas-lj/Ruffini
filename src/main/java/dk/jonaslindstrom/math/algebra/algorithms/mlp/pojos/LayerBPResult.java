package dk.jonaslindstrom.math.algebra.algorithms.mlp.pojos;

import dk.jonaslindstrom.math.algebra.elements.vector.Vector;

public class LayerBPResult {

  private final Vector<Double> error;
  private final Vector<Double> delta;

  public LayerBPResult(Vector<Double> error, Vector<Double> delta) {
    this.error = error;
    this.delta = delta;
  }

  public Vector<Double> getError() {
    return error;
  }

  public Vector<Double> getDelta() {
    return delta;
  }
}
