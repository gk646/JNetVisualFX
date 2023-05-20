package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.DerivativeActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.util.Manual;

import java.util.ArrayList;

/**
 * A java implementation of a NeuralNetwork. Performs the basic {@link Network#forwardPass(float[])} and {@link Network#backPropagation(float[], float[])}
 * to learn from inputData.
 * Allows for various customization through the  {@link  NetworkBuilder}.
 * Supported activation functions: RELU ,SIGMOID.
 */
@Manual(text = """
        A java implementation of a NeuralNetwork. Performs the basic {@link Network#forwardPass(float[])} and {@link Network#backPropagation(float[], float[])}
         * to learn from inputData.
         * Allows for various customization through the  {@link  NetworkBuilder}.
         * Supported activation functions: RELU ,SIGMOID.""")
public final class Network {
    //VARIABLES
    final byte layerCount;
    final float learnRate;
    final byte outputLayerSize;
    final byte inputLayerSize;
    final double[][][] weightMatrix;
    public final int[] layerInfo;
    //OBJECTS
    final NetworkUtils netUtils = new NetworkUtils(this);
    final Layer[] layers;
    final ActivationFunction activeFunc;
    final DerivativeActivationFunction derivativeFunc;

    public Network(NetworkBuilder networkBuilder) {
        this.layerInfo = networkBuilder.getLayerInfo();
        this.layerCount = (byte) layerInfo.length;
        this.outputLayerSize = (byte) layerInfo[layerCount - 1];
        this.inputLayerSize = (byte) layerInfo[0];
        this.activeFunc = networkBuilder.getActiveFunc();
        this.derivativeFunc = DerivativeActivationFunction.valueOf(activeFunc.name());
        this.learnRate = networkBuilder.getLearnRate();

        layers = Layer.createLayers(layerInfo, networkBuilder.getNeuronInitState());
        weightMatrix = Layer.createWeightMatrix(layerInfo, networkBuilder.getWeightInit());

        netUtils.networkIntegrityCheck(this);
    }

    /**
     * Performs a standard forwardPass through the Network.
     *
     * @param inputs a float Array
     * @return a List of both pre- and postActivation values for each Layer (in that order).
     */
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
}
