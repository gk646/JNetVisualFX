package gk646.jnet.neuralnetwork.builder;


public enum ActivationFunction {
    SIGMOID {
        @Override
        public float apply(float x) {
            return (float) (1 / (1 + Math.exp(-x)));
        }
    },
    RELU {
        @Override
        public float apply(float x) {
            return Math.max(0, x);
        }
    };


    public abstract float apply(float x);
}
