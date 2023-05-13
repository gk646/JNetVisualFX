package gk646.jnet.neuralnetwork.builder;

import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.exceptions.IllegalNetworkArguments;

import java.util.List;

public final class NetworkBuilder {
    private List<Integer> layerInfo;
    private ActivationFunction activeFunc;
    private byte learnRate;
    private NeuronInitState neuronInitState;
    private WeightInitState weightInitState;

    public NetworkBuilder(List<Integer> layerInfo, ActivationFunction activeFunc) {
        this.layerInfo = layerInfo;
        this.activeFunc = activeFunc;

        checkBuilderViability();
    }

    public NetworkBuilder setNeuronInitState(NeuronInitState neuronInitState) {
        this.neuronInitState = neuronInitState;
        return this;
    }

    /**
     * Sets the learnRate the NeuralNetwork. Learnrate is the multiplier by which the gradient descent step is multiplied each learning iteration.
     * Making the learnRate too small or too big can have a big impact on the outcome.
     * Recommended values 1-5.
     *
     * @param learnRate an int.
     * @return a reference to this object
     */
    public NetworkBuilder setLearnRate(int learnRate) {
        this.learnRate = (byte) learnRate;
        return this;
    }

    public NetworkBuilder setWeightInitState(WeightInitState weightInitState) {
        this.weightInitState = weightInitState;
        return this;
    }

    public short[] getLayerInfo() {
        short[] temp = new short[layerInfo.size()];
        for (int i = 0; i < layerInfo.size(); i++) {
            temp[i] = layerInfo.get(i).shortValue();
        }
        return temp;
    }

    private void checkBuilderViability() {
        if (layerInfo.size() > 127) throw new IllegalNetworkArguments("Amount of layers must be less than 128");

        for (int num : layerInfo) {
            if (num <= 0) {
                throw new IllegalNetworkArguments("Layer size must be greater than 0");
            } else if (num > 127) {
                throw new IllegalNetworkArguments("Layer size must be less than 128");
            }
        }

        if (activeFunc == null) throw new IllegalNetworkArguments("No activation function!");

        if (learnRate <= 0) throw new IllegalNetworkArguments("Learnrate must be in the range: 1-127");

        if (weightInitState == WeightInitState.RANDOM && (weightInitState.bound > 10 || weightInitState.origin < -10)) {
            throw new IllegalNetworkArguments("Weight initialization bounds are too big! origin: " + weightInitState.origin + " - bound: " + weightInitState.bound);
        }
    }

    public WeightInitState getWeightInit() {
        return weightInitState;
    }

    public ActivationFunction getActiveFunc() {
        return activeFunc;
    }

    public byte getLearnRate() {
        return learnRate;
    }

    public NeuronInitState getNeuronInitState() {
        return neuronInitState;
    }
}
