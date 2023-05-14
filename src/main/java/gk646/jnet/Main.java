package gk646.jnet;


import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.neuralnetwork.builder.WeightInitState;
import gk646.jnet.userinterface.Window;
import javafx.application.Application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> Application.launch(Window.class, args)).start();

        var netBuilder = new NetworkBuilder(List.of(3, 3, 4), ActivationFunction.SIGMOID);
        netBuilder.setNeuronInitState(NeuronInitState.RANDOM).setWeightInitState(WeightInitState.RANDOM);

        var network = new NeuralNetwork(netBuilder);

        network.testInput(new float[]{3, 3, 3});

        network.train(1, new float[]{1, 1, 1}, new float[]{0, 0, 0, 0});


    }
}