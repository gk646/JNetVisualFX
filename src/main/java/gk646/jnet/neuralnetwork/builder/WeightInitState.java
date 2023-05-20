package gk646.jnet.neuralnetwork.builder;

public enum WeightInitState {
    RANDOM(-2f, 2f), ZERO(0, 0);

    float origin;
    float bound;

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

    public float getBound() {
        return bound;
    }

    public float getOrigin() {
        return origin;
    }
}
