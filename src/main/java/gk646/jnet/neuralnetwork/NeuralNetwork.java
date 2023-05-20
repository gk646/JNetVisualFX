package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.util.Manual;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * The wrapper class for the network which allows for higher level concepts like batchTraining and regularization.
 */

public final class NeuralNetwork {
    private Network network;

    @Manual(text = "The NeuralNetwork. Can be built using \"new Network\" after you made a NetBuilder. All the attributes like learn-rate and activation function are changed through the NetBuilder." +
            "This is the intended behavior because once a network is built its in a final state and can only be rebuilt completely.")
    public NeuralNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }

    /**
     * Get the output for a given input. Performs a simple forwardPass through the network.
     * If {@link Log#verbose} is true (default) prints out the result.
     *
     * @param inputs a float array.
     * @return the output array form the forwardPass
     */
    public double[] testInput(double[] inputs) {
        if (inputs.length != network.layers[0].neuronCount) {
            throw new InputMismatchException("Input array doesn't match the size of the input layer!");
        }
        double[] temp = network.forwardPass(inputs).get(network.layerCount - 2)[1];
        if (!Log.verbose) return temp;
        System.out.println(Arrays.toString(temp));
        return temp;
    }

    public void train(double[][] input, double[][] target) {
        if (!network.netUtils.arrayShapeCheck(input, target)) return;
        for (int i = 0; i < input.length; i++) {
            network.backPropagation(input[i], target[i]);
        }
    }

    public void train(int repetitions, double[] input, double[] target) {
        for (int i = 0; i < repetitions; i++) {
            network.backPropagation(input, target);
        }
    }

    public void train(double[] input, double[] target) {
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
