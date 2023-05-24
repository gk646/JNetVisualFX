package gk646.jnet.networks.neuralnetwork;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.util.Manual;

import java.util.InputMismatchException;

/**
 * The wrapper class for the network which allows for higher level concepts like batchTraining and regularization.
 */

public final class NeuralNetwork {
    public static boolean VERBOSE = true;
    static int delayPerStep = 70;
    private final Network network;

    @Manual(text = "The NeuralNetwork. Can be built using \"new Network\" after you made a NetBuilder. All the attributes like learn-rate and activation function are changed through the NetBuilder." +
            "This is the intended behavior because once a network is built its in a final state and can only be rebuilt completely.")
    public NeuralNetwork(NetworkBuilder netBuilder) {
        this.network = new Network(netBuilder);
    }

    public static void setDelayPerStep(int val) {
        delayPerStep = val;
    }

    /**
     * Get the output for a given input. Performs a simple forwardPass through the network.
     * If {@link #VERBOSE} is true (default) prints out the result.
     *
     * @param inputs a float array.
     * @return the output array form the forwardPass
     */
    public double[] out(double[] inputs) {
        if (inputs.length != network.inputLayerSize) {
            Log.logger.logException(InputMismatchException.class, "Input array doesn't match the size of the input layer!");
        }
        UserStatistics.updateStat(UserStatistics.Stat.numberOfForwardPasses, 1);
        return network.forwardPass(inputs);
    }

    public void trainVisual(double[][] input, double[][] target, int repetitions) {
        if (!network.netUtils.arrayShapeCheck(input, target)) return;

        new Thread(() -> {
            for (int i = 0; i < repetitions; i++) {
                for (int j = 0; j < input.length; j++) {
                    network.backPropagationVisual(input[j], target[j]);
                }
                network.netUtils.perRepetitionTasks(input, target);
            }
            network.netUtils.finishTraining(repetitions, input.length);
        }).start();
    }

    public void trainRandom(double[][] input, double[][] target, int repetitions) {
        if (!network.netUtils.arrayShapeCheck(input, target)) return;

        new Thread(() -> {
            for (int i = 0; i < repetitions; i++) {
                for (int j = 0; j < input.length; j++) {
                    int number = NetworkUtils.rng.nextInt(input.length);
                    network.backPropagation(input[number], target[number]);
                }
                network.netUtils.perRepetitionTasks(input, target);
            }
            network.netUtils.finishTraining(repetitions, input.length);
        }).start();
    }

    public void trainSynchronous(double[][] input, double[][] target, int repetitions) {
        if (!network.netUtils.arrayShapeCheck(input, target)) return;

        var worker= new Thread(() -> {
            for (int i = 0; i < repetitions; i++) {
                for (int j = 0; j < input.length; j++) {
                    int number = NetworkUtils.rng.nextInt(input.length);
                    network.backPropagation(input[number], target[number]);
                }
                network.netUtils.perRepetitionTasks(input, target);
            }
            network.netUtils.finishTraining(repetitions, input.length);
        });
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void quicktrain(double[][] input, double[][] target, int repetitions) {
        if (!network.netUtils.arrayShapeCheck(input, target)) return;

        new Thread(() -> {
            for (int i = 0; i < repetitions; i++) {
                for (int j = 0; j < input.length; j++) {
                    network.backPropagation(input[j], target[j]);
                }
                network.netUtils.perRepetitionTasks(input, target);
            }
            network.netUtils.finishTraining(repetitions, input.length);
        }).start();
    }

    public int[] getBounds() {
        return network.layerInfo;
    }

    public Network getNetwork() {
        return network;
    }
}
