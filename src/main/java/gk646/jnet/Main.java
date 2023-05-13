package gk646.jnet;


import gk646.jnet.neuralNetwork.NeuralNetwork;
import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.builder.NeuronInitState;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        var netBuilder = new NetworkBuilder(List.of(3, 4, 4), ActivationFunction.RELU);
        netBuilder.setNeuronInitState(NeuronInitState.RANDOM);


        var network = new NeuralNetwork(netBuilder);

        network.testInput(new float[]{3, 3, 3});
    }
}