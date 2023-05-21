package gk646.jnet.neuralnetwork.builder;

public enum NeuronInitState {
    RANDOM(-0.2f, 0.2f), ZERO(0, 0.00000000001f);

    float origin;
    float bound;

    NeuronInitState(float origin, float bound) {
        this.origin = origin;
        this.bound = bound;
    }

    public static NeuronInitState random(float origin, float bound) {
        NeuronInitState randomState = RANDOM;
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
