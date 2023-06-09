package gk646.jnet.networks.neuralnetwork;

import gk646.jnet.networks.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.networks.neuralnetwork.builder.DerivativeActivationFunction;
import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.Log;

import java.util.Arrays;

public final class Layer {
    final int layerSize;
    final double[] wSums;
    private final double[] output;
    private final double[] input;
    private final double[][] weights;
    private final double[][] dweights;
    private final double[] biases;
    ActivationFunction activeFunc;
    DerivativeActivationFunction derivativeFunc;

    Layer(int inSize, int outSize, NetworkBuilder networkBuilder) {
        this.activeFunc = networkBuilder.getActiveFunc();
        this.derivativeFunc = DerivativeActivationFunction.valueOf(activeFunc.name());

        this.layerSize = outSize;

        this.biases = new double[outSize];
        this.output = new double[outSize];
        this.input = new double[inSize + 1];
        this.wSums = new double[outSize];


        this.weights = new double[inSize + 1][outSize];
        this.dweights = new double[weights.length][outSize];
        for (int i = 0; i < outSize; i++) {
            for (int j = 0; j < inSize + 1; j++) {
                this.weights[j][i] = NetworkUtils.rng.nextDouble(networkBuilder.getWeightInit().getOrigin(), networkBuilder.getWeightInit().getBound());
            }
            biases[i] = NetworkUtils.rng.nextDouble(networkBuilder.getNeuronInitState().getOrigin(), networkBuilder.getNeuronInitState().getBound());
        }
    }

    public static Layer[] createLayers(int[] layerInfo, NetworkBuilder networkBuilder) {
        int layerCount = layerInfo.length;
        if (layerCount < 1) {
            Log.logger.logException(IllegalArgumentException.class, "invalid layer-count");
        }

        Layer[] temp = new Layer[layerCount - 1];

        for (int i = 0; i < layerCount - 1; i++) {
            if (i == layerCount - 2) {
                temp[i] = new Layer(layerInfo[i], layerInfo[i + 1], networkBuilder);
                temp[i].activeFunc = networkBuilder.getLastLayerFunction();
                temp[i].derivativeFunc = DerivativeActivationFunction.valueOf(temp[i].activeFunc.toString());
                break;
            }
            temp[i] = new Layer(layerInfo[i], layerInfo[i + 1], networkBuilder);
        }

        return temp;
    }

    double[] forwardPass(double[] in) {
        System.arraycopy(in, 0, this.input, 0, in.length);
        input[input.length - 1] = 1;
        Arrays.fill(output, 0);
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < input.length; j++) {
                output[i] += weights[j][i] * input[j];
            }
            output[i] += biases[i];
            wSums[i] = output[i];
            output[i] = activeFunc.apply(output[i]);
        }
        return Arrays.copyOf(output, output.length);
    }

    double[] backwardPass(double[] error, double learnRate, double momentum) {
        double[] nextError = new double[input.length];
        for (int i = 0; i < output.length; i++) {
            double d = error[i];
            d *= derivativeFunc.apply(wSums[i]);
            double sum = 0;
            for (int j = 0; j < input.length; j++) {
                nextError[j] += weights[j][i] * d;
                sum += weights[j][i] * d;
                double dw = input[j] * d * learnRate;
                weights[j][i] -= dweights[j][i] * momentum + dw;
                dweights[j][i] = dw;
            }
            biases[i] -= learnRate * d;
        }
        return nextError;
    }

    double[] forwardPassVisual(double[] in, int layerNumber) {
        System.arraycopy(in, 0, this.input, 0, in.length);
        input[input.length - 1] = 1;
        Arrays.fill(output, 0);
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < input.length; j++) {
                if (j < input.length - 1) {
                    if (weights[j][i] > NetworkVisualizer.maxWeight) NetworkVisualizer.maxWeight = weights[j][i];
                    else if (weights[j][i] < NetworkVisualizer.lowestWeight) NetworkVisualizer.lowestWeight = weights[j][i];
                    NetworkVisualizer.activeConnection[layerNumber][i][j] += 25;
                    NetworkUtils.sleep(NeuralNetwork.delayPerStep);
                }
                output[i] += weights[j][i] * input[j];
            }
            //output[i] += neurons[i].bias;
            wSums[i] = output[i];
            output[i] = activeFunc.apply(output[i]);
        }
        return Arrays.copyOf(output, output.length);
    }

    double[] backwardPassVisual(double[] error, double learnRate, double momentum, int layerNumber) {
        double[] nextError = new double[input.length];
        for (int i = 0; i < output.length; i++) {
            double d = error[i];
            d *= derivativeFunc.apply(wSums[i]);
            for (int j = 0; j < input.length; j++) {
                if (j < input.length - 1) {
                    NetworkVisualizer.activeConnection[layerNumber][i][j] -= 25;
                    NetworkUtils.sleep(NeuralNetwork.delayPerStep);
                }
                nextError[j] += weights[j][i] * d;
                double dw = input[j] * d * learnRate;
                weights[j][i] -= dweights[j][i] * momentum + dw;
                dweights[j][i] = dw;
            }
        }
        return nextError;
    }

    public double getWeight(int from, int to) {
        return weights[to][from];
    }
}
