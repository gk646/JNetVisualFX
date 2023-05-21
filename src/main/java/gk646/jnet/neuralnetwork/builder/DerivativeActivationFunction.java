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
    },

    LINEAR {
        @Override
        public double apply(double x) {
            return ActivationFunction.linearFactor;
        }
    },

    SOFTMAX {
        @Override
        public double apply(double x) {
            //TODO
            // Note: The derivative of the softmax function requires a vector of values, not a single value.
            // If you need to use softmax, you might need to handle it elsewhere in your code.
            return x;
        }
    },

    TANH {
        @Override
        public double apply(double x) {
            double tanh = Math.tanh(x);
            return 1 - tanh * tanh;
        }
    };


    public abstract double apply(double x);
}
