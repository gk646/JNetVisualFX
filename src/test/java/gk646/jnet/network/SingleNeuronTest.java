package gk646.jnet.network;

import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SingleNeuronTest {
    double actiF(double x) {
        return x;
    }

    double dactiF(double x) {
        return 1;
    }

    static class Neuron {
        double bias;

        Neuron(double bias) {
            this.bias = bias;
        }

        public static Neuron[] layer(int size, Supplier<Double> method) {
            var temp = new Neuron[size];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = new Neuron(method.get());
            }
            return temp;
        }
    }

    class Layer {
        Neuron[] neurons;
        double[] output;
        double[] input;
        double[][] weights;

        Layer(int inputNeurons, int outputNeurons) {
            this.neurons = Neuron.layer(outputNeurons, () -> -1 + Math.random() * 2);
            this.weights = new double[inputNeurons][outputNeurons];
            this.output = new double[outputNeurons];
            this.input = new double[inputNeurons];

            for (int i = 0; i < outputNeurons; i++) {
                for (int j = 0; j < inputNeurons; j++) {
                    this.weights[j][i] = -0.5 + Math.random();
                }
            }
        }

        private double[] forwardPass(double[] input) {
            System.arraycopy(input, 0, this.input, 0, input.length);
            Arrays.fill(output, 0);
            for (int i = 0; i < output.length; i++) {
                for (int j = 0; j < input.length; j++) {
                    output[i] += weights[j][i];
                }
                output[i] += neurons[i].bias;
                output[i] = actiF(output[i]);
            }
            return Arrays.copyOf(output, output.length);
        }

        public double[] train(double[] error, double learnRate, double momentum) {
            return error;
        }
    }

    Layer[] layers = new Layer[]{new Layer(1, 1), new Layer(1, 1)};


    @Test
    void twoNeuronsSquare() {
        double[][] input = new double[][]{{2}, {4}, {6}, {8}};
        double[][] output = new double[][]{{4}, {8}, {12}, {16}};
        double learnRate = 0.5;
        double momentum = 0.5;
        // for (int i = 0; i < input.length; i++) {
        double[] referenceInput = input[0];
        for (int k = 0; k < layers.length; k++) {
            referenceInput = layers[k].forwardPass(referenceInput);
        }
        // }

        System.out.println(Arrays.toString(referenceInput));
        /*
        System.out.println(weight);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < input.length; j++) {
                double error;
                double[] neuronsOutput = new double[neurons.length];
                for (int k = 0; k < neurons.length; k++) {
                    neuronsOutput[k] = actiF(input[j] * neurons[k]);
                    System.out.println(neuronsOutput[k]);
                    error = neuronsOutput[k] - output[j];
                    double outputGradient = error * dactiF(neuronsOutput[k]);
                    neurons[k] -= outputGradient * learnRate;
                }
            }


        }

         */
    }

    @Test
    void singleNeuronDouble() {
        double[] input = new double[]{2, 4, 6, 8};
        double[] target = new double[]{4, 8, 12, 16};
        double learnRate = 0.04;
        double weight = -1 + Math.random() * 2;
        double bias = -1 + Math.random() * 2;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < input.length; j++) {
                double weightedSum = input[j] * weight + bias;
                double output = actiF(weightedSum);
                double error = output - target[j];
                double outputGradient = error * dactiF(weightedSum);

                weight -= outputGradient * learnRate * input[j];
                bias -= outputGradient * learnRate;
            }
        }

        System.out.println(actiF(2 * weight + bias));
        assertTrue(Math.abs(4 - actiF(2 * weight + bias)) < 0.1);
        assertTrue(Math.abs(8 - actiF(4 * weight + bias)) < 0.2);


       // var network = new NeuralNetwork(new NetworkBuilder(List.of(1), ActivationFunction.SIGMOID));
    }
}
