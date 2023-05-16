package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;

import java.util.List;

public final class Playground {

    public static NetworkBuilder networkBuilder;

    public static NeuralNetwork neuralNetwork = new NeuralNetwork(new NetworkBuilder(List.of(4,4,4), ActivationFunction.SIGMOID));

    private Playground() {}


    public static void reset(){
        networkBuilder = null;
        neuralNetwork = null;
    }

}
