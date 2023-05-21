package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NeuronInitState;

public final class Neuron {

    float bias;

    Neuron(float bias) {
        this.bias = bias;
    }

    public static Neuron[] layer(int neuronCount, NeuronInitState neuronInitState) {
        Neuron[] temp = new Neuron[neuronCount];
        for (byte i = 0; i < neuronCount; i++) {
            temp[i] = new Neuron(NetworkUtils.rng.nextFloat(neuronInitState.getOrigin(), neuronInitState.getBound()));
        }
        return temp;
    }
}
