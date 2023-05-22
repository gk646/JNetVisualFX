package gk646.jnet.networks.neuralnetwork;

import gk646.jnet.networks.neuralnetwork.builder.NeuronInitState;

public final class Neuron {

    double bias;

    Neuron(double bias) {
        this.bias = bias;
    }

    public static Neuron[] layer(int neuronCount, NeuronInitState neuronInitState) {
        Neuron[] temp = new Neuron[neuronCount];
        for (byte i = 0; i < neuronCount; i++) {
            temp[i] = new Neuron(NetworkUtils.rng.nextDouble(neuronInitState.getOrigin(), neuronInitState.getBound()));
        }
        return temp;
    }
}
