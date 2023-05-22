package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.util.datastructures.Matrix;

import java.util.HashMap;
import java.util.Map;

public final class Playground {

    public static NetworkBuilder networkBuilder;

    public static NeuralNetwork neuralNetwork;

    public static final Map<String, Matrix> playgroundLists = new HashMap<>();

    private Playground() {
    }


    public static void reset() {
        networkBuilder = null;
        neuralNetwork = null;
    }
}
