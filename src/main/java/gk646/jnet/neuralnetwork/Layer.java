package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.neuralnetwork.builder.WeightInitState;

public final class Layer {
    byte neuronCount;
    Neuron[] neurons;

    Layer(byte neuronCount) {
        this.neuronCount = neuronCount;
        neurons = Neuron.createNeurons(neuronCount);
    }

    public static Layer[] createLayers(short[] layerInfo, NeuronInitState neuronInit) {
        NetworkUtils.logger.info("Creating layers");

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

    public static float[][][] createWeightMatrix(short[] layerInfo, WeightInitState weightInit) {
        NetworkUtils.logger.info("Creating weight matrix");

        short layerCount = (short) layerInfo.length;
        if (layerCount < 2) {
            NetworkUtils.logger.severe("Invalid layerCount. There should be at least two layers.");
            throw new IllegalArgumentException("Invalid layerCount. There should be at least two layers.");
        }

        // We have layerCount - 1 weight matrices, because the weights connect pairs of layers
        float[][][] temp = new float[layerCount - 1][][];

        for (byte i = 0; i < layerCount - 1; i++) {
            temp[i] = new float[layerInfo[i]][layerInfo[i + 1]];
            if (weightInit != WeightInitState.RANDOM) continue;
            for (int j = 0; j < temp[i].length; j++) {
                for (int k = 0; k < temp[i][j].length; k++) {
                    temp[i][j][k] = NetworkUtils.rng.nextFloat(weightInit.origin, weightInit.bound);
                }
            }
        }
        return temp;
    }
}
