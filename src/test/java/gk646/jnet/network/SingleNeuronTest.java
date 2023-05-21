package gk646.jnet.network;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SingleNeuronTest {
    float actiF(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    float dactiF(float x) {
        return actiF(x) * (1 - actiF(x));
    }

    static class Neuron {
        float bias;

        Neuron(float bias) {
            this.bias = bias;
        }

        public static Neuron[] layer(int size, Supplier<Float> method) {
            var temp = new Neuron[size];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = new Neuron(method.get());
            }
            return temp;
        }
    }

    class Layer {
        Neuron[] neurons;
        float[] output;
        float[] input;
        float[][] weights;
        float[][] dweights;
        boolean isLastLayer = false;

        Layer(int inputNeurons, int outputNeurons) {
            this.neurons = Neuron.layer(outputNeurons, () -> (float) 0);
            this.weights = new float[inputNeurons + 1][outputNeurons];
            this.output = new float[outputNeurons];
            this.input = new float[inputNeurons + 1];
            this.dweights = new float[weights.length][outputNeurons];
            for (int i = 0; i < outputNeurons; i++) {
                for (int j = 0; j < inputNeurons + 1; j++) {
                    this.weights[j][i] = (float) ((Math.random() - 0.5f) * 4f);
                }
            }
        }

        private float[] forwardPass(float[] in) {
            System.arraycopy(in, 0, this.input, 0, in.length);
            input[input.length - 1] = 1;
            Arrays.fill(output, 0);
            for (int i = 0; i < output.length; i++) {
                for (int j = 0; j < input.length; j++) {
                    output[i] += weights[j][i] * input[j];
                }
                //output[i] += neurons[i].bias;
                if (!isLastLayer) {
                    output[i] = (float) (1 / (1 + Math.exp(-output[i])));
                }
            }
            return Arrays.copyOf(output, output.length);
        }

        public float[] train(float[] error, float learnRate, float momentum) {
            float[] nextError = new float[input.length];
            for (int i = 0; i < output.length; i++) {
                float d = error[i];
                if (!isLastLayer) {
                    d *= output[i] * (1 - output[i]);
                }
                for (int j = 0; j < input.length; j++) {
                    nextError[j] += weights[j][i] * d;
                    float dw = input[j] * d * learnRate;
                    weights[j][i] += dweights[j][i] * momentum + dw;
                    dweights[j][i] = dw;
                }
            }
            return nextError;
        }
    }

    Layer[] layers = new Layer[]{new Layer(2, 2), new Layer(2, 1)};


    private float[] run(float[] input) {
        float[] referenceInput = input;
        for (int i = 0; i < layers.length; i++) {
            referenceInput = layers[i].forwardPass(referenceInput);
        }
        return referenceInput;
    }


    private void train(float[] input, float[] target, float learnRate, float momentum) {
        float[] calcError = run(input);
        float[] error = new float[calcError.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = target[i] - calcError[i];
        }
        for (int i = layers.length - 1; i >= 0; i--) {
            error = layers[i].train(error, learnRate, momentum);
        }
    }

    @Test
    void twoNeuronsSquare() {
        layers[layers.length - 1].isLastLayer = true;
        float[][] input = new float[][]{new float[]{0, 0}, new float[]{0, 1}, new float[]{1, 0}, new float[]{1, 1}};
        float[][] target = new float[][]{new float[]{0}, new float[]{1}, new float[]{1}, new float[]{0}};
        float learnRate = 0.4f;
        float momentum = 0.6f;
        int repses = 5000;
        for (int reps = 0; reps < repses; reps++) {
            for (int i = 0; i < input.length; i++) {
                int randomIndex = (int) (Math.random() * input.length);
                train(input[randomIndex], target[randomIndex], learnRate, momentum);
            }
        }
        System.out.println(Arrays.toString(run(new float[]{1, 1})));
        assertTrue(Math.abs(run(new float[]{1, 1})[0]) < 0.1);
        assertTrue(Math.abs(1 - run(new float[]{1, 0})[0]) < 0.1);
        assertTrue(Math.abs(run(new float[]{0, 0})[0]) < 0.1);
        assertTrue(Math.abs(1 - run(new float[]{0, 1})[0]) < 0.1);
    }

    @Test
    void singleNeuronDouble() {
        double[] input = new double[]{2, 4, 6, 8};
        double[] target = new double[]{4, 8, 12, 16};
        double learnRate = 0.02;
        double weight = -0.5 + Math.random() * 1;
        double bias = -0.5 + Math.random() * 1;

        for (int i = 0; i < 220; i++) {
            for (int j = 0; j < input.length; j++) {
                double weightedSum = input[j] * weight + bias;
                double output = weightedSum;
                double error = output - target[j];
                double outputGradient = error * 1;

                weight -= outputGradient * learnRate * input[j];
                bias -= outputGradient * learnRate;
            }
        }
        System.out.println(2 * weight + bias);
        assertTrue(Math.abs(4 - 2 * weight + bias) < 0.1);
        assertTrue(Math.abs(8 - 4 * weight + bias) < 0.2);
    }
}
