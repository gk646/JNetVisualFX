package gk646.jnet.neuralNetwork;

import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.exceptions.NetworkIntegrityException;

import java.util.Arrays;

import static gk646.jnet.neuralNetwork.NetworkUtils.logger;

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

        networkIntegrityCheck();
    }


    public float[] testInput(float[] inputs) {
        if (inputs.length != layers[0].neuronCount) {
            throw new IllegalArgumentException("Input array doesn't match the size of the input layer!");
        }
        float [] result = forwardPass(inputs);
        System.out.println(Arrays.toString(result));
        return result;
    }

    private float[] forwardPass(float[] inputs) {
        float[] layerInput = inputs;

        for (byte i = 0; i < layerCount - 1; i++) {
            float[] layerOutput = new float[layerInfo[i + 1]];

            for (int j = 0; j < layerOutput.length; j++) {  // Iterate over neurons in next layer
                float weightedSum = 0;
                for (int k = 0; k < layerInput.length; k++) { // Iterate over neurons in current layer
                    weightedSum += layerInput[k] * weightMatrix[i][k][j];
                }
                weightedSum += layers[i+1].neurons[j].bias;
                layerOutput[j] = activeFunc.apply(weightedSum);
            }
            layerInput = layerOutput;
        }
        return layerInput;
    }



    private float dotProduct(float[] inputs, short[] weights) {
        float result = 0;
        for (int i = 0; i < inputs.length; i++) {
            result += inputs[i] * weights[i];
        }
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        int max = 0;
        for (int i = 0; i < layerCount; i++) {
            if (layerInfo[i] > max) {
                max = layerInfo[i];
            }
        }
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < layerCount; j++) {
                if (i < layerInfo[j]) {
                    sb.append("o ");
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
            if (weightMatrix[i].length != layerInfo[i] || weightMatrix[i][0].length != layerInfo[i + 1]) {
                logger.severe("Weight matrix dimensions don't match layerInfo!");
                throw new NetworkIntegrityException("Weight matrix dimensions don't match layerInfo!");
            }
        }
        logger.info("Printing network structure:");
        System.out.println(this);

        netUtils.sleep(20);

        logger.info("Printing weight matrix structure: \n");

        netUtils.print3DArray(weightMatrix);

        netUtils.sleep(20);

        logger.info("Network integrity checks successful!\n");
    }
}
