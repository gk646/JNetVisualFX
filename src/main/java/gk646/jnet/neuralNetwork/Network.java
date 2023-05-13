package gk646.jnet.neuralNetwork;

import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;

import java.util.ArrayList;

public final class Network {
    NetworkUtils netUtils = new NetworkUtils();
    Layer[] layers;
    byte layerCount;
    byte outputLayerSize;
    short[][][] weightMatrix;
    short[] layerInfo;
    ActivationFunction activeFunc;

    public Network(NetworkBuilder networkBuilder) {
        this.layerInfo = networkBuilder.getLayerInfo();
        this.layerCount = (byte) layerInfo.length;
        this.outputLayerSize = (byte) layerInfo[layerCount - 1];
        this.activeFunc = networkBuilder.getActiveFunc();


        layers = Layer.createLayers(layerInfo);
        weightMatrix = Layer.createWeightMatrix(layerInfo);

        netUtils.networkIntegrityCheck(this);
    }


    ArrayList<float[]> forwardPass(float[] inputs) {
        float[] layerInput = inputs;
        ArrayList<float[]> layerOutputs = new ArrayList<>(layerCount);
        for (byte i = 0; i < layerCount - 1; i++) {
            float[] layerOutput = new float[layerInfo[i + 1]];

            for (byte j = 0; j < layerOutput.length; j++) {  // Iterate over neurons in next layer
                float weightedSum = 0;
                for (byte k = 0; k < layerInput.length; k++) { // Iterate over neurons in current layer
                    weightedSum += layerInput[k] * weightMatrix[i][k][j];
                }
                weightedSum += layers[i + 1].neurons[j].bias;
                layerOutput[j] = activeFunc.apply(weightedSum);
            }
            layerOutputs.add(layerOutput);
            layerInput = layerOutput;

        }
        return layerOutputs;
    }


    void backPropagation(ArrayList<float[]> input, ArrayList<float[]> target) {

    }
}
