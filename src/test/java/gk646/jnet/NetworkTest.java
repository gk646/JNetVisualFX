package gk646.jnet;

import gk646.jnet.neuralNetwork.Network;
import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.builder.NeuronInitState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NetworkTest {


    Network network = new Network(new NetworkBuilder(List.of(3, 3, 3), ActivationFunction.SIGMOID).setNeuronInitState(NeuronInitState.RANDOM));

    @Test
    void testNetworkOutput() {
        assertTrue(Arrays.equals(new float[]{0.5f, 0.5f, 0.5f}, network.testInput(new float[]{3, 3, 3})));
    }
}
