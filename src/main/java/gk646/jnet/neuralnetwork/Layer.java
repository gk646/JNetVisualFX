package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.DerivativeActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.neuralnetwork.builder.NeuronInitState;
import gk646.jnet.userinterface.terminal.Log;

import java.util.Arrays;

public final class Layer {
    final ActivationFunction activeFunc;
    final DerivativeActivationFunction derivativeFunc;
    final int layerSize;
    Neuron[] neurons;
    private final double[] output;
    private final double[] input;
    private final double[][] weights;
    private final double[][] dweights;
    final double[] wSums;
    boolean isLastLayer = false;

    Layer(int inputNeurons, int outputNeurons, ActivationFunction activeFunc, NeuronInitState neuronInitState) {
        this.activeFunc = activeFunc;
        this.derivativeFunc = DerivativeActivationFunction.valueOf(activeFunc.name());

        this.layerSize = outputNeurons;


        this.output = new double[outputNeurons];
        this.input = new double[inputNeurons + 1];
        this.wSums = new double[outputNeurons];

        this.neurons = Neuron.layer(outputNeurons, neuronInitState);

        this.weights = new double[inputNeurons + 1][outputNeurons];
        this.dweights = new double[weights.length][outputNeurons];
        for (int i = 0; i < outputNeurons; i++) {
            for (int j = 0; j < inputNeurons + 1; j++) {
                this.weights[j][i] = ((Math.random() - 0.5f) * 4f);
            }
        }
    }


    double[] forwardPass(double[] in) {
        System.arraycopy(in, 0, this.input, 0, in.length);
        input[input.length - 1] = 1;
        Arrays.fill(output, 0);
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < input.length; j++) {
                output[i] += weights[j][i] * input[j];
            }
            //output[i] += neurons[i].bias;
            if (!isLastLayer) {
                output[i] =  (1 / (1 + Math.exp(-output[i])));
            }
        }
        return Arrays.copyOf(output, output.length);
    }

    double[] backwardPass(double[] error, double learnRate, double momentum) {
        double[] nextError = new double[input.length];
        for (int i = 0; i < output.length; i++) {
            double d = error[i];
            if (!isLastLayer) {
                d *= output[i] * (1 - output[i]);
            }
            for (int j = 0; j < input.length; j++) {
                nextError[j] += weights[j][i] * d;
                double dw = input[j] * d * learnRate;
                weights[j][i] += dweights[j][i] * momentum + dw;
                dweights[j][i] = dw;
            }
        }
        return nextError;
    }

    public static Layer[] createLayers(int[] layerInfo, NetworkBuilder networkBuilder) {
        int layerCount = layerInfo.length;
        if (layerCount < 1) {
            Log.logger.logException(IllegalArgumentException.class, "invalid layer-count");
        }

        Layer[] temp = new Layer[layerCount - 1];

        for (int i = 0; i < layerCount - 1; i++) {
            if (i == layerCount - 2) {
                temp[i] = new Layer(layerInfo[i], layerInfo[i + 1], networkBuilder.getLastLayerFunction(), networkBuilder.getNeuronInitState());
                temp[i].isLastLayer = true;
                break;
            }
            temp[i] = new Layer(layerInfo[i], layerInfo[i + 1], networkBuilder.getActiveFunc(), networkBuilder.getNeuronInitState());
        }

        return temp;
    }
}
