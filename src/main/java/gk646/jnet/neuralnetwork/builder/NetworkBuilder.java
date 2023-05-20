package gk646.jnet.neuralnetwork.builder;

import gk646.jnet.neuralnetwork.exceptions.IllegalNetworkArguments;
import gk646.jnet.util.Manual;

import java.util.List;


/**
 * A helper class to specify the network parameters. Besides layerInfo and the activationFunction customization is optional.
 * LayerInfo is a list where each entry is the number of neurons for that layer, including input and output layer!.
 */
@Manual(text = " A helper class to specify the network parameters. Besides layerInfo and the activationFunction customization is optional.\n" +
        "LayerInfo is a list where each entry is the number of neurons for that layer, including input and output layer!.")
public final class NetworkBuilder {
    private final List<Integer> layerInfo;
    private final ActivationFunction activeFunc;
    private float learnRate = 1;
    private NeuronInitState neuronInitState;
    private WeightInitState weightInitState;

    /**
     * The NetworkBuilder will internally check for viability of the given arguments
     *
     * @param layerInfo  a list, each entry specifying the number of neurons for that layer.
     * @param activeFunc the activationFunction for the network
     */
    @Manual(text = "Reusable building block for your Networks - Syntax: new NetBuilder((<List of numbers>),<activationFunction>)  || e.g NetBuilder((3,3,3),sigmoid)")
    public NetworkBuilder(List<Integer> layerInfo, ActivationFunction activeFunc) {
        this.layerInfo = layerInfo;
        this.activeFunc = activeFunc;

        checkBuilderViability();
    }

    /**
     * Sets the neuron initialization state which controls how the initial bias of the neurons pre-training is initialized.
     * Supported values RANDOM and ZERO.
     * Recommended value: RANDOM
     *
     * @param neuronInitState the initState
     * @return a reference to this object
     */

    public NetworkBuilder setNeuronInitState(NeuronInitState neuronInitState) {
        this.neuronInitState = neuronInitState;
        checkBuilderViability();
        return this;
    }

    /**
     * Sets the learnRate the NeuralNetwork. Learn-rate is the multiplier by which the gradient descent step is multiplied each learning iteration.
     * Making the learnRate too small or too big can have a big impact on the outcome.
     * Recommended values 1-5.
     *
     * @param learnRate an int.
     * @return a reference to this object
     */
    @Manual(text = """
             Sets the learnRate the NeuralNetwork. Learn-rate is the multiplier by which the gradient descent step is multiplied each learning iteration.
                  Making the learnRate too small or too big can have a big impact on the outcome.
                 Recommended values 1-5.
                 
                  @param learnRate an int.
                  @return a reference to this object
                 
            """)
    public NetworkBuilder setLearnRate(float learnRate) {
        this.learnRate =  learnRate;
        checkBuilderViability();
        return this;
    }

    /**
     * Sets the weight initialization state which controls how the initial weights of the network weight matrix are set.
     * This is a very crucial element and can lead to a virtually non-functioning network if set wrong.<br>
     * Supported values RANDOM (default range: -0.1, 0.1) and ZERO(0,0) and {@link WeightInitState#random(float, float)}.
     * Recommended value: RANDOM
     *
     * @param weightInitState the weightInitState
     * @return a reference to this object
     */
    @Manual(text = """
            Sets the weight initialization state which controls how the initial weights of the network weight matrix are set.
                 * This is a very crucial element and can lead to a virtually non-functioning network if set wrong.<br>
                 * Supported values RANDOM (default range: -0.1, 0.1) and ZERO(0,0) and {@link WeightInitState#random(float, float)}.
                 * Recommended value: RANDOM
                 *
                 * @param weightInitState the weightInitState
                 * @return a reference to this object""")
    public NetworkBuilder setWeightInitState(WeightInitState weightInitState) {
        this.weightInitState = weightInitState;
        checkBuilderViability();
        return this;
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

    public int[] getLayerInfo() {
        return layerInfo.stream().mapToInt(Integer::intValue).toArray();
    }

    public WeightInitState getWeightInit() {
        return weightInitState;
    }

    public ActivationFunction getActiveFunc() {
        return activeFunc;
    }

    public float getLearnRate() {
        return learnRate;
    }

    public NeuronInitState getNeuronInitState() {
        return neuronInitState;
    }
}
