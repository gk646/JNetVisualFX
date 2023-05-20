package gk646.jnet.neuralnetwork;

public final class Neuron {

    public byte bias;

    Neuron() {

    }

    public static Neuron[] createNeurons(byte neuronCount) {
        Neuron[] temp = new Neuron[neuronCount];
        for (byte i = 0; i < neuronCount; i++) {
            temp[i] = new Neuron();
        }
        return temp;
    }
}
