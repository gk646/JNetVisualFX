package gk646.jnet.neuralnetwork.builder;

public enum NeuronInitState {
    RANDOM(-0.2f, 0.2f), ZERO(0, 0.0000000000001f);

    double origin;
    double bound;

    NeuronInitState(double origin, double bound) {
        this.origin = origin;
        this.bound = bound;
    }

    public static NeuronInitState random(double origin, double bound) {
        NeuronInitState randomState = RANDOM;
        randomState.origin = origin;
        randomState.bound = bound;
        return randomState;
    }

    public double getBound() {
        return bound;
    }

    public double getOrigin() {
        return origin;
    }
}
