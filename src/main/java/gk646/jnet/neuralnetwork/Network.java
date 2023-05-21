package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.LossFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.util.Manual;

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


        this.layerCount = (byte) layerInfo.length;
        this.outputLayerSize = (byte) layerInfo[layerCount - 1];
        this.inputLayerSize = (byte) layerInfo[0];

        this.lossFunction = networkBuilder.getLossFunction();
        this.learnRate = networkBuilder.getLearnRate();
        this.momentum = networkBuilder.getMomentum();

        layers = Layer.createLayers(layerInfo, networkBuilder);

        netUtils.networkIntegrityCheck(this);
    }

    /**
     * Performs a standard forwardPass through the Network.
     *
     * @param input a double Array
     * @return a List of both pre- and postActivation values for each Layer (in that order).
     */

     double[] forwardPass(double[] input) {
        double[] referenceInput = input;
        for (int i = 0; i < layers.length; i++) {
            referenceInput = layers[i].forwardPass(referenceInput);
        }
        return referenceInput;
    }


     void backPropagation(double[] input, double[] target) {
        double[] calcError = forwardPass(input);
        double[] error = new double[calcError.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = target[i] - calcError[i];
        }
        for (int i = layers.length - 1; i >= 0; i--) {
            error = layers[i].backwardPass(error, learnRate, momentum);
        }
    }
    
    
}
