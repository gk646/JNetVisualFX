package gk646.jnet.networks.neuralnetwork.builder;


public enum ActivationFunction {
    SIGMOID {
        @Override
        public double apply(double x) {
            return 1 / (1 + Math.exp(-x));
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
            return linearFactor * x;
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
            return Math.tanh(x);
        }
    };


    static int linearFactor = 1;

    static void setLinearFactor(int f) {
        linearFactor = f;
    }

    public abstract double apply(double x);
}
