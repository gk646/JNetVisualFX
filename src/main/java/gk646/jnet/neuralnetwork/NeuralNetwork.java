package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.util.Manual;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * The wrapper class for the network which allows for higher level concepts like batchTraining and regularization.
 */
@Manual(text = "The wrapper class for the network which allows for higher level concepts like batchTraining and regularization.\n")
public final class NeuralNetwork {
    private Network network;

    @Manual(text = "The Neural Network. Built using a NetBuilder and calling NetBuilder.build()")
    public NeuralNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }

    /**
     * Get the output for a given input. Performs a simple forwardPass through the network.
     * If {@link NetworkUtils#verbose} is true (default) prints out the result.
     *
     * @param inputs a float array.
     * @return the output array form the forwardPass
     */
    public float[] testInput(float[] inputs) {
        if (inputs.length != network.layers[0].neuronCount) {
            throw new InputMismatchException("Input array doesn't match the size of the input layer!");
        }
        float[] temp = network.forwardPass(inputs).get(network.layerCount - 2)[1];
        if (!NetworkUtils.verbose) return temp;
        System.out.println(Arrays.toString(temp));
        return temp;
    }

    public void train(int repetitions, float[][] data) {
        for (int i = 0; i < repetitions; i++) {
            //network.backPropagation();
        }
    }

    public void train(int repetitions, float[] input, float[] target) {
        for (int i = 0; i < repetitions; i++) {
            network.backPropagation(input, target);
        }
    }

    public void train(float[] input, float[] target) {
        network.backPropagation(input, target);
    }

    public void train(int repetitions, float[][][] data) {
        for (int i = 0; i < repetitions; i++) {
            //network.backPropagation();
        }
    }


    public void changeNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }

    public Network getNetwork() {
        return network;
    }
}
