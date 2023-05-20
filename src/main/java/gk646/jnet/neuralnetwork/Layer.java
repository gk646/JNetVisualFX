package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.neuralnetwork.builder.WeightInitState;
import gk646.jnet.userinterface.terminal.Log;

public final class Layer {
    final byte neuronCount;
    public final Neuron[] neurons;

    Layer(byte neuronCount) {
        this.neuronCount = neuronCount;
        neurons = Neuron.createNeurons(neuronCount);
    }

    public static Layer[] createLayers(int[] layerInfo, NeuronInitState neuronInit) {
        Log.logger.info("Creating layers");
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

    public static double[][][] createWeightMatrix(int[] layerInfo, WeightInitState weightInit) {
        Log.logger.info("Creating weight matrix");
        short layerCount = (short) layerInfo.length;
        if (layerCount < 2) {
            Log.logger.severe("Invalid layerCount. There should be at least two layers.");
            throw new IllegalArgumentException("Invalid layerCount. There should be at least two layers.");
        }
        // We have layerCount - 1 weight matrices, because the weights connect pairs of layers
        double[][][] temp = new double[layerCount - 1][][];

        for (byte i = 0; i < layerCount - 1; i++) {
            temp[i] = new double[layerInfo[i]][layerInfo[i + 1]];
            if (weightInit != WeightInitState.RANDOM) continue;
            for (int j = 0; j < temp[i].length; j++) {
                for (int k = 0; k < temp[i][j].length; k++) {
                    temp[i][j][k] = NetworkUtils.rng.nextFloat(weightInit.getOrigin(), weightInit.getBound());
                }
            }
        }
        return temp;
    }
}
