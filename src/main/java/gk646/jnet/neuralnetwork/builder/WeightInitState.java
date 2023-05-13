package gk646.jnet.neuralnetwork.builder;

public enum WeightInitState {
    RANDOM(-0.1f, 0.11f), ZERO(0, 0);

    public float origin;
    public float bound;

    WeightInitState(float origin, float bound) {
        this.origin = origin;
        this.bound = bound;
    }

    public static WeightInitState random(float origin, float bound) {
        WeightInitState randomState = RANDOM;
        randomState.origin = origin;
        randomState.bound = bound;
        return randomState;
    }
}
