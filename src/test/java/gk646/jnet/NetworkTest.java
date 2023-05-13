package gk646.jnet;

import gk646.jnet.neuralNetwork.Network;
import gk646.jnet.neuralNetwork.NetworkUtils;
import gk646.jnet.neuralNetwork.NeuralNetwork;
import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.builder.NeuronInitState;
import gk646.jnet.neuralNetwork.exceptions.IllegalNetworkArguments;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NetworkTest {

    static {
        NetworkUtils.verbose = false;
        NetworkUtils.removeHandler();
    }

    @Test
    void defaultOutputTest() {
        var network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        assertArrayEquals(new float[]{0.5f, 0.5f, 0.5f}, network.testInput(new float[]{3, 3, 3}));
    }

    @Test
    void networkStructureTest() {
        var network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        network.testInput(new float[]{0.5f, 0.5f, 0.5f});

        network = new NeuralNetwork(new NetworkBuilder(List.of(4, 3, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        network.testInput(new float[]{0.5f, 0.5f, 0.5f, 0.5f});

        network = new NeuralNetwork(new NetworkBuilder(List.of(1, 4, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        network.testInput(new float[]{0.5f});

        assertThrows(IllegalNetworkArguments.class, () -> new NetworkBuilder(List.of(-1, 3, 3), ActivationFunction.SIGMOID));

        final var network1 = new NeuralNetwork(new NetworkBuilder(List.of(2, 4, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        assertThrows(InputMismatchException.class, () -> network1.testInput(new float[]{0.5f}));
    }
}
