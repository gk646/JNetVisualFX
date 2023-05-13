package gk646.jnet.neuralNetwork;

import java.util.List;

public final class Layer {
    byte neuronCount;
    Neuron[] neurons;

    Layer(byte neuronCount) {
        this.neuronCount = neuronCount;

        neurons = Neuron.createNeurons(neuronCount);
    }

    public static Layer[] createLayers(short[] layerInfo) {
        Network.logger.info("Creating layers");

        byte layerCount = (byte) layerInfo.length;
        if (layerCount < 1 || layerCount > layerInfo.length) {
            throw new IllegalArgumentException("Invalid layerCount");
        }

        Layer[] temp = new Layer[layerCount];

        for (byte i = 0; i < layerCount; i++) {
            temp[i] = new Layer((byte) layerInfo[i]);
        }
        return temp;
    }

    public static short[][][] createWeightMatrix(short[] layerInfo) {
        Network.logger.info("Creating weight matrix");

        short layerCount = (short) layerInfo.length;
        if (layerCount < 2) {
            Network.logger.severe("Invalid layerCount. There should be at least two layers.");
            throw new IllegalArgumentException("Invalid layerCount. There should be at least two layers.");
        }

        // We have layerCount - 1 weight matrices, because the weights connect pairs of layers
        short[][][] temp = new short[layerCount - 1][][];

        for (byte i = 0; i < layerCount - 1; i++) {
            temp[i] = new short[layerInfo[i]][layerInfo[i+1]];
        }
        return temp;
    }
}
