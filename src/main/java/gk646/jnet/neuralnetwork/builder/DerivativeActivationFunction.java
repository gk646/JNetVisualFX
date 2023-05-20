package gk646.jnet.neuralnetwork.builder;

public enum DerivativeActivationFunction {

    SIGMOID {
        @Override
        public double apply(double x) {
            return  (1 / (1 + Math.exp(-x)) * (1 - (1 / (1 + Math.exp(-x)))));
        }
    },
    RELU {
        @Override
        public double apply(double x) {
            return x > 0 ? 1 : 0;
        }
    };

    public abstract double apply(double x);
}
