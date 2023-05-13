package gk646.jnet.neuralNetwork;

import gk646.jnet.neuralNetwork.builder.ActivationFunction;
import gk646.jnet.neuralNetwork.builder.NetworkBuilder;
import gk646.jnet.neuralNetwork.exceptions.NetworkIntegrityException;

import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Network {
    public static final Logger logger = Logger.getLogger(Network.class.getName());

    static {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return
                        " [" + logRecord.getLevel() + "] " +
                                logRecord.getMessage() + "\n";
            }
        });


        logger.addHandler(consoleHandler);
    }

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


    public void testInput(float[] inputs) {
        if (inputs.length != layers[0].neuronCount) {
            throw new IllegalArgumentException("Input array doesn't match the size of the input layer!");
        }
        System.out.println(Arrays.toString(forwardPass(inputs)));
    }

    private float[] forwardPass(float[] inputs) {

        float[] layerInput = inputs;

        for (byte i = 0; i < layerCount - 1; ++i) {
            float[] layerOutput = new float[layerInfo[i + 1]];

            for (int j = 0; j < layerOutput.length; j++) {
                float weightedSum = dotProduct(layerInput, weightMatrix[i][j]) + layers[i].neurons[j].bias;
                layerOutput[j] = activeFunc.apply(weightedSum);
            }
            layerInput = layerOutput;
        }
        return layerInput;
    }
    public void print3DArray(short[][][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println("Slice " + i + ":");
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    System.out.print(array[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
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
                if (layerInfo[j] < max && i < max - layerInfo[j]) {

                }
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
        try {
            Thread.sleep(25);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        print3DArray(new short[5][2][2]);
        logger.info("Network integrity checks successful!\n");
    }
}
