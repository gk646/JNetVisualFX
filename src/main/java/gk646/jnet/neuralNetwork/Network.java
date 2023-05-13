package gk646.jnet.neuralNetwork;

import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.exceptions.NetworkIntegrityException;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class Network {
    public static final Logger logger = Logger.getLogger(Network.class.getName());

    Layer[] layers;
    byte layerCount;
    byte outputLayerSize;
    short[][][] weightMatrix;
    List<Integer> layerInfo;
    ActivationFunction activeFunc;

    public Network(NetworkBuilder networkBuilder) {
        this.layerInfo = networkBuilder.getLayerInfo();
        this.layerCount = (byte) (layerInfo.size() - 1);
        this.outputLayerSize = layerInfo.get(layerCount).byteValue();
        this.activeFunc = networkBuilder.getActiveFunc();


        layers = Layer.createLayers(layerInfo);
        weightMatrix = Layer.createWeightMatrix(layerInfo);

        networkIntegrityCheck();
    }


    public float[] forwardPass(float[] inputs) {
        float[] layerInput = inputs;
        for (byte i = 0; i < layerCount; i++) {
            float[] layerOutput = new float[weightMatrix[i].length];

            for (byte j = 0; j < weightMatrix[i].length; j++) {
                float neuronSum = 0;
                for (byte k = 0; k < weightMatrix[i][j].length; k++) {
                    neuronSum += layerInput[k] * weightMatrix[i][j][k];
                }
                layerOutput[j] = activeFunc.apply(neuronSum);
            }
            layerInput = layerOutput;
        }
        return layerInput;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int maxNeurons = Collections.max(layerInfo);

        for (int i = 0; i < maxNeurons; i++) {
            for (Integer neurons : layerInfo) {
                if (neurons > i) {
                    sb.append("O ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    private void networkIntegrityCheck() {
        logger.info("Performing network integrity checks");
        for (int i = 0; i < layerCount - 1; i++) {
            if (weightMatrix[i].length != layerInfo.get(i) || weightMatrix[i][0].length != layerInfo.get(i + 1)) {
                logger.severe("Weight matrix dimensions don't match layerInfo!");
                throw new NetworkIntegrityException("Weight matrix dimensions don't match layerInfo!");
            }
        }


        logger.info("Network integrity checks successful!");
    }
}
