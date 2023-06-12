package gk646.jnet.network;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.networks.neuralnetwork.NeuralNetwork;
import gk646.jnet.networks.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NetworkTest {

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return
                        " [" + logRecord.getLevel() + "] " +
                                logRecord.getMessage() + "\n";
            }
        });
        Log.logger.addHandler(handler);
    }

    /*
        @Test
        void defaultOutputTest() {
            var network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.ZERO));
            assertArrayEquals(new double[]{0.5f, 0.5f, 0.5f}, network.testInput(new double[]{3, 3, 3}));

            network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.RELU).setNeuronInitState(NeuronInitState.ZERO));
            assertArrayEquals(new double[]{0, 0, 0}, network.testInput(new double[]{3, 3, 3}));
        }

        @Test
        void networkStructureTest() {
            var network = new NeuralNetwork(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
            network.testInput(new double[]{0.5f, 0.5f, 0.5f});

            network = new NeuralNetwork(new NetworkBuilder(List.of(4, 3, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
            network.testInput(new double[]{0.5f, 0.5f, 0.5f, 0.5f});

            network = new NeuralNetwork(new NetworkBuilder(List.of(1, 4, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
            //network.testInput(new double[]{0.5f});

            assertThrows(IllegalNetworkArguments.class, () -> new NetworkBuilder(List.of(-1, 3, 3), ActivationFunction.SIGMOID));

            final var network1 = new NeuralNetwork(new NetworkBuilder(List.of(2, 4, 1), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));
            assertThrows(InputMismatchException.class, () -> network1.testInput(new double[]{0.5f}));
        }



     */
    @Test
    void backPropagationTestXOR() {
        new UserStatistics();
        var list = new ArrayList<Integer>();
        NeuralNetwork.verbose = false;
        for (int p = 0; p < 1000; p++) {
            // Create the network with 2 inputs, 2 hidden neurons, and 1 output
            NeuralNetwork network = new NeuralNetwork(new NetworkBuilder(new int[]{2, 2, 1}, ActivationFunction.SIGMOID).setLearnRate(0.35));

            // XOR input and output pairs
            double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
            double[][] outputs = {{0}, {1}, {1}, {0}};

            // Train the network
            int repetitions = 1000; // adjust this as necessary
            network.trainSynchronous(inputs, outputs, repetitions);

            // Test the network
            double epsilon = 0.1f; // tolerance for the test
            for (int i = 0; i < inputs.length; i++) {
                double[] networkOutput = network.out(inputs[i]);
                if ((Math.abs(networkOutput[0] - outputs[i][0]) < epsilon)) {
                    list.add(1);
                    continue;
                }
                list.add(0);
                //assertTrue(Math.abs(networkOutput[0] - outputs[i][0]) < epsilon);
            }
        }
        var success = (double) list.stream().filter(i -> i == 1).count() / list.size();
        assertTrue(success > 0.9);
        System.out.println(success);
    }
}
