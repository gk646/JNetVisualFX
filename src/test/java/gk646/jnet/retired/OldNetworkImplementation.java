package gk646.jnet.retired;

public class OldNetworkImplementation {
    /*
    ArrayList<double[][]> forwardPass(double[] inputs) {
        double[] layerInput = inputs;
        ArrayList<double[][]> layerOutputsAndInputs = new ArrayList<>(layerCount);
        for (byte i = 0; i < layerCount - 1; i++) {
            double[] layerOutput = new double[layerInfo[i + 1]];
            double[] layerInputForNextLayer = new double[layerInfo[i + 1]]; //Pre-activation values for next layer

            for (byte j = 0; j < layerOutput.length; j++) {  // Iterate over neurons in next layer
                double weightedSum = 0;
                for (byte k = 0; k < layerInput.length; k++) { // Iterate over neurons in current layer
                    weightedSum += layerInput[k] * weightMatrix[i][k][j];
                }
                layerInputForNextLayer[j] = weightedSum + layers[i].neurons[j].bias;
                layerOutput[j] = activeFunc.apply(layerInputForNextLayer[j]);
            }
            layerOutputsAndInputs.add(new double[][]{layerInputForNextLayer, layerOutput});
            layerInput = layerOutput;
        }
        return layerOutputsAndInputs;
    }

    void backPropagation(double[] input, double[] target) {
        ArrayList<double[][]> layerOutputsAndInputs = forwardPass(input);
        double[] error = new double[layerOutputsAndInputs.get(layerOutputsAndInputs.size() - 1)[1].length];
        for (byte i = 0; i < error.length; i++) {
            error[i] = layerOutputsAndInputs.get(layerOutputsAndInputs.size() - 1)[1][i] - target[i];
        }

        for (int layerIndex = layerCount - 2; layerIndex > -1; layerIndex--) {

            double[] nextError = new double[layerInfo[layerIndex]];

            for (byte currentLayer = 0; currentLayer < layerInfo[layerIndex + 1]; currentLayer++) {

                double outputGradient = error[currentLayer] * derivativeFunc.apply(layerOutputsAndInputs.get(layerIndex)[0][currentLayer]);

                for (byte nextLayer = 0; nextLayer < layerInfo[layerIndex]; nextLayer++) {

                    nextError[nextLayer] += weightMatrix[layerIndex][nextLayer][currentLayer] * outputGradient;

                    double weightGradient = outputGradient * layerOutputsAndInputs.get(Math.max(0, layerIndex - 1))[1][nextLayer];

                    weightMatrix[layerIndex][nextLayer][currentLayer] -= learnRate * weightGradient;
                }
                layers[layerIndex + 1].neurons[currentLayer].bias -= learnRate * outputGradient;
            }

            error = nextError;
        }
    }

     */
}
