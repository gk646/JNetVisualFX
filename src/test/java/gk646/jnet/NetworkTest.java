package gk646.jnet;

import gk646.jnet.neuralnetwork.NetworkUtils;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.neuralnetwork.builder.WeightInitState;
import gk646.jnet.neuralnetwork.exceptions.IllegalNetworkArguments;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NetworkTest {

    static {
        NetworkUtils.verbose = false;
        NetworkUtils.removeHandler();
    }

    @Test
    void defaultOutputTest() {
        var network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
        assertArrayEquals(new float[]{0.5f, 0.5f, 0.5f}, network.testInput(new float[]{3, 3, 3}));

        network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.RELU).setNeuronInitState(NeuronInitState.RANDOM));
        assertArrayEquals(new float[]{0, 0, 0}, network.testInput(new float[]{3, 3, 3}));
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


    @Test
    void backPropagationTestXOR() {
        // Create the network with 2 inputs, 2 hidden neurons, and 1 output
        NeuralNetwork network = new NeuralNetwork(new NetworkBuilder(List.of(2, 2, 1), ActivationFunction.SIGMOID).setWeightInitState(WeightInitState.RANDOM).setNeuronInitState(NeuronInitState.RANDOM));

        // XOR input and output pairs
        float[][] inputs = { {0, 0}, {0, 1}, {1, 0}, {1, 1} };
        float[][] outputs = { {0}, {1}, {1}, {0} };

        // Train the network
        int repetitions = 5000; // adjust this as necessary
        for (int epoch = 0; epoch < repetitions; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                network.train(inputs[i], outputs[i]);
            }
        }
        NetworkUtils.verbose = true;
        // Test the network
        float epsilon = 0.1f; // tolerance for the test
        for (int i = 0; i < inputs.length; i++) {
            float[] networkOutput = network.testInput(inputs[i]);
            for (int j = 0; j < networkOutput.length; j++) {
                assertTrue(Math.abs(networkOutput[j] - outputs[i][j]) < epsilon);
            }
        }
        NetworkUtils.verbose = false;
    }

}
