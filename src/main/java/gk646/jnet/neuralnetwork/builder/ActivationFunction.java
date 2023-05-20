package gk646.jnet.neuralnetwork.builder;


public enum ActivationFunction {
    SIGMOID {
        @Override
        public double apply(double x) {
            return (1 / (1 + Math.exp(-x)));
        }
    },
    RELU {
        @Override
        public double apply(double x) {
            return Math.max(0, x);
        }
    },

    LINEAR {
        @Override
        public double apply(double x) {
            return factor * x;
        }
    },

    SOFTMAX {
        @Override
        public double apply(double x) {
            //TODO
            return x;
        }
    },
    TANH {
        @Override
        public double apply(double x) {
            //TODO
            return x;
        }
    };
    int factor;

    ActivationFunction(int... factor) {
        this.factor = 1;
        if (factor.length!= 0) {
            this.factor = factor[0];
        }
    }

    public abstract double apply(double x);
}
