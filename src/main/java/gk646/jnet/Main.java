package gk646.jnet;


import gk646.jnet.neuralNetwork.Network;
import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.builder.NeuronInitState;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.print("Hello and welcome!\n");

        Network network = new Network(new NetworkBuilder(List.of(1, 4, 3, 4), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));


        network.testInput(new float[]{1});
    }
}