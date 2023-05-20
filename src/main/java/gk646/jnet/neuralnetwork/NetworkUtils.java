package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.exceptions.NetworkIntegrityException;
import gk646.jnet.userinterface.terminal.Log;

import java.util.InputMismatchException;
import java.util.Random;

public final class NetworkUtils {
    final Network network;
    public static final Random rng = new Random(System.nanoTime());

    public NetworkUtils(Network network) {
        this.network = network;
    }

    public void print3DArray(double[][][] array) {
        StringBuilder sb = new StringBuilder();
        if (!Log.verbose) return;
        for (int i = 0; i < array.length; i++) {
            System.out.println("Weight Layer Pair: " + i);
            sb.append("Weight Layer Pair: ").append(i);
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    double num = array[i][j][k];
                    if (num < 0) {
                        System.out.printf("%.2f | ", num);
                        sb.append(String.format("%.2f | ", num));
                    } else {
                        System.out.printf(" %.2f | ", num);
                        sb.append(String.format("%.2f | ", num));
                    }
                }
                System.out.println();
                sb.append("\n");
            }
            System.out.println();
            sb.append("\n");
            //Log.addLogText(sb.toString());
            sb = new StringBuilder();
        }
    }

    public void printNeuronBias(Layer[] layers) {
        for (int i = 0; i < layers.length; i++) {
            System.out.println("NeuronLayer: "+i);
            for (int j = 0; j < layers[i].neuronCount; j++) {
                System.out.println("|"+layers[i].neurons[j].bias+"|");
            }
            System.out.println();
        }
    }

    /**
     * Performs {@link Thread#sleep(long)} for the given time.
     * Skips the duration if  {@link Log#verbose} is false:
     *
     * @param millis amount of milliseconds as int.
     */
    public void sleep(int millis) {
        if (!Log.verbose) return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public void printNetwork() {
        if (!Log.verbose) return;
        Log.addLogText("");
        StringBuilder sb = new StringBuilder();
        int max = 0;
        for (int i = 0; i < network.layerCount; i++) {
            if (network.layerInfo[i] > max) {
                max = network.layerInfo[i];
            }
        }
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < network.layerCount; j++) {
                if (i < network.layerInfo[j]) {
                    sb.append("o ");
                } else {
                    sb.append("  ");
                }
            }
            Log.addLogText(sb.toString());
            sb = new StringBuilder();
        }
    }

    /**
     * Performs various checks regarding the structural integrity of the network.
     */
    void networkIntegrityCheck(Network network) {
        Log.logger.info("Performing network integrity checks");
        for (int i = 0; i < network.layerCount - 1; i++) {
            if (network.weightMatrix[i].length != network.layerInfo[i] || network.weightMatrix[i][0].length != network.layerInfo[i + 1]) {
                Log.logger.severe("Weight matrix dimensions don't match layerInfo!");
                throw new NetworkIntegrityException("Weight matrix dimensions don't match layerInfo!");
            }
        }
        Log.logger.info("Printing network structure:");
        printNetwork();
        sleep(20);

        Log.logger.info("Printing weight matrix structure: \n");
        network.netUtils.print3DArray(network.weightMatrix);
        network.netUtils.sleep(20);

        Log.logger.info("Network integrity checks successful!\n");
    }

    public boolean arrayShapeCheck(double[][] input, double[][] target) {
        if (input.length != network.inputLayerSize || target.length != network.outputLayerSize) {
            return Log.logger.logException(InputMismatchException.class, "Given input matrix does not match shape of target matrix");
        }
        for (int i = 0; i < input.length; i++) {
            if (input[i].length != network.inputLayerSize && target[i].length != network.outputLayerSize) {
                return Log.logger.logException(InputMismatchException.class, "Given input matrix does not match shape of target matrix");
            }
        }
        return true;
    }

    public boolean arrayShapeCheck(float[] input, float[] target) {
        if (input.length != network.inputLayerSize || target.length != network.outputLayerSize) {
            return Log.logger.logException(InputMismatchException.class, "Given input matrix does not match shape of target matrix");
        }
        return true;
    }
}
