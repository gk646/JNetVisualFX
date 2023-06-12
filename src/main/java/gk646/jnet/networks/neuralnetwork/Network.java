package gk646.jnet.networks.neuralnetwork;

import gk646.jnet.networks.neuralnetwork.builder.LossFunction;
import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.util.Manual;

import java.util.Arrays;

/**
 * A java implementation of a NeuralNetwork. Performs the basic {@link Network#forwardPass(double[])} and {@link Network#backPropagation(double[], double[])}
 * to learn from inputData.
 * Allows for various customization through the  {@link  NetworkBuilder}.
 * Supported activation functions: RELU ,SIGMOID.
 */
@Manual(text = """
        A java implementation of a NeuralNetwork. Performs the basic {@link Network#forwardPass(double[])} and {@link Network#backPropagation(double[], double[])}
         * to learn from inputData.
         * Allows for various customization through the  {@link  NetworkBuilder}.
         * Supported activation functions: RELU ,SIGMOID.""")
public final class Network {
    final byte layerCount;
    final double learnRate;
    final byte outputLayerSize;
    final byte inputLayerSize;
    final int[] layerInfo;
    final NetworkUtils netUtils = new NetworkUtils(this);
    final Layer[] layers;
    final LossFunction lossFunction;
    final double momentum;

    public Network(NetworkBuilder networkBuilder) {
        this.layerInfo = networkBuilder.getLayerInfo();
        NetworkVisualizer.setMaxNeurons(Arrays.stream(layerInfo).max().orElse(0));

        this.layerCount = (byte) layerInfo.length;
        this.outputLayerSize = (byte) layerInfo[layerCount - 1];
        this.inputLayerSize = (byte) layerInfo[0];

        this.lossFunction = networkBuilder.getLossFunction();
        this.learnRate = networkBuilder.getLearnRate();
        this.momentum = networkBuilder.getMomentum();

        layers = Layer.createLayers(layerInfo, networkBuilder);
        NetworkVisualizer.updateConnectionMatrix(networkBuilder.getLayerInfo());
    }

    /**
     * Performs a standard forwardPass through the Network.
     *
     * @param input a double Array
     * @return an array of both pre- and postActivation values for each Layer (in that order).
     */

    double[] forwardPass(double[] input) {
        double[] referenceInput = input;
        for (final Layer layer : layers) {
            referenceInput = layer.forwardPass(referenceInput);
        }
        return referenceInput;
    }

    void backPropagation(double[] input, double[] target) {
        double[] calcError = forwardPass(input);
        double[] error = new double[calcError.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = lossFunction.apply(calcError[i], target[i]);
        }
        for (int i = layers.length - 1; i >= 0; i--) {
            error = layers[i].backwardPass(error, learnRate, momentum);
        }
    }

    void backPropagationVisual(double[] input, double[] target) {
        double[] calcError = forwardPassVisual(input);
        double[] error = new double[calcError.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = lossFunction.apply(calcError[i], target[i]);
        }
        for (int i = layers.length - 1; i >= 0; i--) {
            error = layers[i].backwardPassVisual(error, learnRate, momentum, i);
        }
    }

    private double[] forwardPassVisual(double[] input) {
        double[] referenceInput = input;
        int layerNumber = 0;
        for (final Layer layer : layers) {
            referenceInput = layer.forwardPassVisual(referenceInput, layerNumber++);
        }
        return referenceInput;
    }
}
