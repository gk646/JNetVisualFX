package gk646.jnet.neuralNetwork;

import gk646.jnet.neuralNetwork.builder.NetworkBuilder;

import java.util.Arrays;
import java.util.InputMismatchException;

public final class NeuralNetwork {

    private Network network;


    public NeuralNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }

    public float[] testInput(float[] inputs) {
        if (inputs.length != network.layers[0].neuronCount) {
            throw new InputMismatchException("Input array doesn't match the size of the input layer!");
        }
        float[] temp = network.forwardPass(inputs).get(network.layerCount-2);
        if (!NetworkUtils.verbose) return temp;
        System.out.println(Arrays.toString(temp));
        return temp;
    }

    public void train(int repetitions, float[][] data) {
        for (int i = 0; i < repetitions; i++) {
            //network.backPropagation();
        }
    }

    public void train(int repetitions, float[] data) {
        for (int i = 0; i < repetitions; i++) {
            //network.backPropagation();
        }
    }

    public void train(int repetitions, float[][][] data) {
        for (int i = 0; i < repetitions; i++) {
            //network.backPropagation();
        }
    }


    public void changeNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }
}
