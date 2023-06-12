package gk646.jnet.networks.neuralnetwork;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.Log;

import java.util.InputMismatchException;
import java.util.Random;

public final class NetworkUtils {

    public static final Random rng = new Random(System.nanoTime());
    static final String ARRAY_MISMATCH = "given matrices do not have the same length";
    static final String NETWORK_MISMATCH = "given matrices do not match network bounds";
    final Network network;

    public NetworkUtils(Network network) {
        this.network = network;
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public void perRepetitionTasks(double[][] input, double[][] target) {
        double error = 0;
        double[] output;
        for (int i = 0; i < input.length; i++) {
            output = network.forwardPass(input[i]);
            for (int j = 0; j < output.length; j++) {
                error += network.lossFunction.apply(output[j], target[i][j]);
            }
        }

        if (NeuralNetwork.verbose) {
            Log.logger.info(network.lossFunction + ": " + String.format("%.4f", error));
        }
    }

    public void finishTraining(int repetitions, int inputLength) {
        UserStatistics.updateStat(UserStatistics.Stat.numberOfForwardPasses, repetitions + repetitions * inputLength);
        UserStatistics.updateStat(UserStatistics.Stat.numberOfBackPropagations, repetitions);
        UserStatistics.updateStat(UserStatistics.Stat.networksTrained, 1);
    }


    public void printNetwork() {
        if (!NeuralNetwork.verbose) return;
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

    public boolean arrayShapeCheck(double[][] input, double[][] target) {
        if (input.length != target.length) {
            return Log.logger.logException(InputMismatchException.class, ARRAY_MISMATCH);
        }
        for (int i = 0; i < input.length; i++) {
            if (input[i].length != network.inputLayerSize || target[i].length != network.outputLayerSize) {
                return Log.logger.logException(InputMismatchException.class, NETWORK_MISMATCH);
            }
        }
        return true;
    }

    public boolean arrayShapeCheck(double[] input, double[] target) {
        if (input.length != target.length) {
            return Log.logger.logException(InputMismatchException.class, ARRAY_MISMATCH);
        }
        if (input.length != network.inputLayerSize || target.length != network.outputLayerSize) {
            return Log.logger.logException(InputMismatchException.class, NETWORK_MISMATCH);
        }
        return true;
    }
}
