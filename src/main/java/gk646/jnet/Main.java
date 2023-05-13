package gk646.jnet;


import gk646.jnet.neuralNetwork.Network;
import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.builder.NeuronInitState;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Network network = new Network(new NetworkBuilder(List.of(3,4,4), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));


        network.testInput(new float[]{3,3,3});
    }
}