package gk646.jnet.networks.neuralnetwork.builder;

public enum WeightInitState {
    RANDOM(-0.35f, 0.35f), ZERO(0, 0.0000000000001);

    double origin;
    double bound;

    WeightInitState(double origin, double bound) {
        this.origin = origin;
        this.bound = bound;
    }

    public static WeightInitState random(double origin, double bound) {
        WeightInitState randomState = RANDOM;
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
