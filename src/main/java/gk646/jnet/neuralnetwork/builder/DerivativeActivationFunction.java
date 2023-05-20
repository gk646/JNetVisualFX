package gk646.jnet.neuralnetwork.builder;

public enum DerivativeActivationFunction {

    SIGMOID {
        @Override
        public double apply(double x) {
            return ActivationFunction.SIGMOID.apply(x) * (1 - ActivationFunction.SIGMOID.apply(x));
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
