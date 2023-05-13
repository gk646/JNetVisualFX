package gk646.jnet.neuralNetwork;

public final class Neuron {

    byte bias;

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
