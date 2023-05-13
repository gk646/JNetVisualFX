package gk646.jnet.neuralnetwork.builder;

public enum DerivativeActivationFunction {

    SIGMOID {
        @Override
        public float apply(float x) {
            return (float) (1 / (1 + Math.exp(-x)) * (1 - (1 / (1 + Math.exp(-x)))));
        }
    },
    RELU {
        @Override
        public float apply(float x) {
            return x > 0 ? 1 : 0;
        }
    };

    public abstract float apply(float x);
}
