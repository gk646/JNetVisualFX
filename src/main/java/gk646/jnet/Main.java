package gk646.jnet;


import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.neuralnetwork.builder.WeightInitState;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        var netBuilder = new NetworkBuilder(List.of(3, 3, 4), ActivationFunction.SIGMOID);
        netBuilder.setNeuronInitState(NeuronInitState.RANDOM).setWeightInitState(WeightInitState.random(-1,1));

        var network = new NeuralNetwork(netBuilder);

        network.testInput(new float[]{3, 3, 3});


        network.train(1, new float[]{1, 1, 1}, new float[]{0, 0, 0, 0});
    }
}