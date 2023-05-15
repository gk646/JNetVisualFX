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
@Manual(text = "A java implementation of a NeuralNetwork. Performs the basic {@link Network#forwardPass(float[])} and {@link Network#backPropagation(float[], float[])}\n" +
        " * to learn from inputData.\n" +
        " * Allows for various customization through the  {@link  NetworkBuilder}.\n" +
        " * Supported activation functions: RELU ,SIGMOID.")
public final class Network {
    //VARIABLES
    byte layerCount;
    byte learnRate;
    byte outputLayerSize;
    float[][][] weightMatrix;
    short[] layerInfo;
    //OBJECTS
    NetworkUtils netUtils = new NetworkUtils();
    Layer[] layers;
    ActivationFunction activeFunc;
    DerivativeActivationFunction derivativeFunc;

    @Manual(text = "heya")
    public Network(NetworkBuilder networkBuilder) {
        this.layerInfo = networkBuilder.getLayerInfo();
        this.layerCount = (byte) layerInfo.length;
        this.outputLayerSize = (byte) layerInfo[layerCount - 1];
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
    ArrayList<float[][]> forwardPass(float[] inputs) {
        float[] layerInput = inputs;
        ArrayList<float[][]> layerOutputsAndInputs = new ArrayList<>(layerCount);
        for (byte i = 0; i < layerCount - 1; i++) {
            float[] layerOutput = new float[layerInfo[i + 1]];
            float[] layerInputForNextLayer = new float[layerInfo[i + 1]]; //Pre-activation values for next layer

            for (byte j = 0; j < layerOutput.length; j++) {  // Iterate over neurons in next layer
                float weightedSum = 0;
                for (byte k = 0; k < layerInput.length; k++) { // Iterate over neurons in current layer
                    weightedSum += layerInput[k] * weightMatrix[i][k][j];
                }
                layerInputForNextLayer[j] = weightedSum + layers[i + 1].neurons[j].bias;
                layerOutput[j] = activeFunc.apply(layerInputForNextLayer[j]);
            }
            layerOutputsAndInputs.add(new float[][]{layerInputForNextLayer, layerOutput});
            layerInput = layerOutput;
        }
        return layerOutputsAndInputs;
    }

    void backPropagation(float[] input, float[] target) {
        ArrayList<float[][]> layerOutputsAndInputs = forwardPass(input);
        float[] error = new float[layerOutputsAndInputs.get(layerOutputsAndInputs.size() - 1)[1].length];
        for (byte i = 0; i < error.length; i++) {
            error[i] = layerOutputsAndInputs.get(layerOutputsAndInputs.size() - 1)[1][i] - target[i];
        }

        for (int layerIndex = layerCount - 2; layerIndex >= 0; layerIndex--) {
            float[] nextError = new float[layerInfo[layerIndex]]; // Error for the next layer

            for (byte neuronIndex = 0; neuronIndex < layerInfo[layerIndex + 1]; neuronIndex++) {

                float outputGradient = 2 * error[neuronIndex] * derivativeFunc.apply(layerOutputsAndInputs.get(layerIndex)[0][neuronIndex]);
                for (byte prevNeuronIndex = 0; prevNeuronIndex < layerInfo[layerIndex]; prevNeuronIndex++) {

                    // Update weights and compute next layer's error
                    nextError[prevNeuronIndex] += weightMatrix[layerIndex][prevNeuronIndex][neuronIndex] * outputGradient;
                    weightMatrix[layerIndex][prevNeuronIndex][neuronIndex]
                            -= learnRate * outputGradient * layerOutputsAndInputs.get(layerIndex - 1 >= 0 ? layerIndex - 1 : layerIndex)[1][prevNeuronIndex];
                }
                // Update biases
                layers[layerIndex + 1].neurons[neuronIndex].bias -= learnRate * outputGradient;
            }

            error = nextError;
        }
    }
}
