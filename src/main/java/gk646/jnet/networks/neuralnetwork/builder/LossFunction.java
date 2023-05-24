package gk646.jnet.networks.neuralnetwork.builder;

public enum LossFunction {

    MEAN_SQUARED_AVERAGE_ERROR {
        @Override
        public double apply(double predictedOutput, double targetOutput) {
            return 2 * (predictedOutput - targetOutput);
        }
    },
    MEAN_AVERAGE_ERROR() {
        @Override
        public double apply(double predictedOutput, double targetOutput) {
            return predictedOutput - targetOutput;
        }
    };


    public abstract double apply(double predictedOutput, double targetOutput);
}
