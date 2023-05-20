package gk646.jnet.neuralnetwork.builder;


public enum ActivationFunction {
    SIGMOID {
        @Override
        public double apply(double x) {
            return  (1 / (1 + Math.exp(-x)));
        }
    },
    RELU {
        @Override
        public double apply(double x) {
            return Math.max(0, x);
        }
    };


    public abstract double apply(double x);
}
