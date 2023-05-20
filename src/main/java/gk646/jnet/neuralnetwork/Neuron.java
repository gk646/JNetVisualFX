package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NeuronInitState;

public final class Neuron {

    public float bias;

    Neuron(float bias) {
        this.bias = bias;
    }

    public static Neuron[] createNeurons(byte neuronCount, NeuronInitState state) {
        Neuron[] temp = new Neuron[neuronCount];
        for (byte i = 0; i < neuronCount; i++) {
            if (state == NeuronInitState.RANDOM) {
                temp[i] = new Neuron(NetworkUtils.rng.nextFloat(-0.1f, 0.1f));
            } else {
                temp[i] = new Neuron(0);
            }
        }
        return temp;
    }
}
