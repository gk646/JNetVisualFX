package gk646.jnet.neuralnetwork.builder;

public enum LossFunction {

    MEAN_SQUARED {
        @Override
        public double apply(double predictedOutput, double targetOutput) {
            return 2 * predictedOutput - targetOutput;
        }
    },
    MEAN() {
        @Override
        public double apply(double predictedOutput, double targetOutput) {
            return predictedOutput - targetOutput;
        }
    };


    public abstract double apply(double predictedOutput, double targetOutput);
}
