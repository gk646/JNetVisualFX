package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;

public final class Playground {

    public static NetworkBuilder networkBuilder;

    public static NeuralNetwork neuralNetwork;

    private Playground() {
    }


    public static void reset() {
        networkBuilder = null;
        neuralNetwork = null;
    }
}
