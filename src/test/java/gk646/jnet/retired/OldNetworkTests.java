package gk646.jnet.retired;

public class OldNetworkTests {
    /*
    static int[] layerInfo = new int[]{2, 2, 1};
    static double[][][] weightMatrix = Layer.createWeightMatrix(layerInfo, WeightInitState.RANDOM);
    double learnRate = 0.01f;
    int layerCount = layerInfo.length;
    ActivationFunction activeFunc = ActivationFunction.SIGMOID;
    DerivativeActivationFunction derivativeFunc = DerivativeActivationFunction.SIGMOID;

    Layer[] layers = Layer.createLayers(layerInfo, NeuronInitState.RANDOM);
    static double[][][] gradientsMatrix = new double[weightMatrix.length][][];

    static {
        for (int i = 0; i < weightMatrix.length; i++) {
            gradientsMatrix[i] = new double[weightMatrix[i].length][weightMatrix[i][0].length];
        }
    }

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

    private void backPropagation(double[] input, double[] target) {
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

    private void newBack(double[] input, double[] target) {
        ArrayList<double[][]> layerOutputsAndInputs = forwardPass(input);
        for (int layerIndex = layerCount - 2; layerIndex > -1; layerIndex--) {
            double[] nextError = new double[layerInfo[layerIndex]];

            for (int prevNIndex = 0; prevNIndex < layerInfo[layerIndex + 1]; prevNIndex++) {
                double outputGradient = LossFunction.MEAN_SQUARED.apply(layerOutputsAndInputs.get(layerOutputsAndInputs.size() - 1)[1][prevNIndex], target[prevNIndex])
                        * derivativeFunc.apply(layerOutputsAndInputs.get(layerIndex)[0][prevNIndex]);

                for (int activNIndex = 0; activNIndex < layerInfo[layerIndex]; activNIndex++) {
                    nextError[activNIndex] += weightMatrix[layerIndex][prevNIndex][activNIndex] * outputGradient;
                }
            }
        }
    }

    double computeCost(double[] output, double[] target) {
        if (output.length != target.length) {
            throw new IllegalArgumentException("Output and target arrays must be the same length");
        }
        double sum = 0;
        for (int i = 0; i < output.length; i++) {
            double diff = output[i] - target[i];
            sum += diff * diff;
        }
        return sum / output.length;
    }


    @Test
    void gradientChecking() {
        // Assuming that computeCost is a function that computes the cost of your neural network
        // We'll define a small epsilon value for our numerical gradient approximation
        double epsilon = 1e-4f;

        double[] input = new double[]{1, 1};
        double[] target = new double[]{0};
        // We'll iterate over each layer of weights in the network
        for (int layerIndex = 0; layerIndex < layerCount - 1; layerIndex++) {
            // Then for each neuron's weights in the current layer
            for (int neuronIndex = 0; neuronIndex < weightMatrix[layerIndex].length; neuronIndex++) {
                for (int weightIndex = 0; weightIndex < weightMatrix[layerIndex][neuronIndex].length; weightIndex++) {
                    // Store the original weight
                    double originalWeight = weightMatrix[layerIndex][neuronIndex][weightIndex];

                    // Increase the weight by epsilon
                    weightMatrix[layerIndex][neuronIndex][weightIndex] += epsilon;
                    // Forward propagate and compute the cost
                    ArrayList<double[][]> increasedWeightOutputs = forwardPass(input);
                    double increasedWeightCost = computeCost(increasedWeightOutputs.get(increasedWeightOutputs.size() - 1)[1], target);

                    // Decrease the weight by epsilon
                    weightMatrix[layerIndex][neuronIndex][weightIndex] = originalWeight - epsilon;
                    // Forward propagate and compute the cost
                    ArrayList<double[][]> decreasedWeightOutputs = forwardPass(input);
                    double decreasedWeightCost = computeCost(decreasedWeightOutputs.get(decreasedWeightOutputs.size() - 1)[1], target);

                    // Calculate the numerical gradient
                    double numericalGradient = (increasedWeightCost - decreasedWeightCost) / (2f * epsilon);

                    // Reset the weight to its original value
                    weightMatrix[layerIndex][neuronIndex][weightIndex] = originalWeight;

                    // Compute the backpropagation
                    backPropagation(input, target);
                    // Get the analytic gradient from the backpropagation, note that we assume you are storing the gradients in a similar structure to the weights
                    double analyticGradient = gradientsMatrix[layerIndex][neuronIndex][weightIndex];

                    // Check if the gradients are close
                    if (Math.abs(numericalGradient - analyticGradient) > 1e-5) {
                        System.out.println("Gradients are very different! " +
                                "Numerical: " + numericalGradient +
                                ", Analytical: " + analyticGradient);
                    }
                }
            }
        }
    }
}


 */
}
