package gk646.jnet.networks.neuralnetwork.builder;

import gk646.jnet.networks.neuralnetwork.exceptions.IllegalNetworkArguments;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.util.Manual;


/**
 * A helper class to specify the network parameters. Besides layerInfo and the activationFunction customization is optional.
 * LayerInfo is a list where each entry is the number of neurons for that layer, including input and output layer!.
 */

public final class NetworkBuilder {
    private int[] layerInfo;
    private ActivationFunction activeFunc;
    private double learnRate = 0.5;
    private NeuronInitState neuronInitState = NeuronInitState.RANDOM;
    private WeightInitState weightInitState = WeightInitState.RANDOM;
    private LossFunction lossFunction = LossFunction.MEAN_AVERAGE_ERROR;
    private double momentum = 0.5;
    private ActivationFunction layerLayerActivationFunction = ActivationFunction.LINEAR;

    /**
     * The NetworkBuilder will internally check for viability of the given arguments
     *
     * @param layerInfo  a list, each entry specifying the number of neurons for that layer.
     * @param activeFunc the activationFunction for the network
     */
    @Manual(text = "Reusable building block for your Networks - Syntax: new NetBuilder([<List of numbers>],<activationFunction>)  || e.g NetBuilder((3,3,3),sigmoid)")
    public NetworkBuilder(int[] layerInfo, ActivationFunction activeFunc) {
        this.layerInfo = layerInfo;
        this.activeFunc = activeFunc;

        checkBuilderViability();
    }

    /**
     * Sets the layerStructure of the NeuralNetwork. The first layer is always treated as input and the last always as output layer.
     * Valid in the range 1 - 127.
     *
     * @param layerInfo a new list
     * @return a reference to this object
     */
    public NetworkBuilder setNetworkSize(int[] layerInfo) {
        this.layerInfo = layerInfo;
        checkBuilderViability();
        return this;
    }

    /**
     * Sets the activationFunction for all layers except the last. By default, the last layer is {@link ActivationFunction#LINEAR}.
     */
    public NetworkBuilder setActivationFunction(ActivationFunction activeFunc) {
        this.activeFunc = activeFunc;
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

    public NetworkBuilder setLayerLayerFunction(ActivationFunction function) {
        this.layerLayerActivationFunction = function;
        checkBuilderViability();
        return this;
    }

    private void checkBuilderViability() {
        if (layerInfo.length > 127) {
            Log.logger.logException(IllegalNetworkArguments.class, "Amount of layers must be less than 128");
        }

        for (int num : layerInfo) {
            if (num <= 0) {
                Log.logger.logException(IllegalNetworkArguments.class, "Layer size must be greater than 0");
            } else if (num > 127) {
                Log.logger.logException(IllegalNetworkArguments.class, "Layer size must be less than 128");
            }
        }

        if (activeFunc == null) {
            Log.logger.logException(IllegalNetworkArguments.class, "No activation function!");
        }

        if (learnRate <= 0) {
            Log.logger.logException(IllegalNetworkArguments.class, "Learn-rate must be in the range: 1-127");
        }

        if (weightInitState == WeightInitState.RANDOM && (weightInitState.bound > 10 || weightInitState.origin < -10)) {
            Log.logger.logException(IllegalNetworkArguments.class, "Weight initialization bounds are too big! origin: " + weightInitState.origin + " - bound: " + weightInitState.bound);
        }
    }

    public int[] getLayerInfo() {
        return layerInfo;
    }

    public WeightInitState getWeightInit() {
        return weightInitState;
    }

    public ActivationFunction getActiveFunc() {
        return activeFunc;
    }

    public double getLearnRate() {
        return learnRate;
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
    public NetworkBuilder setLearnRate(double learnRate) {
        this.learnRate = learnRate;
        checkBuilderViability();
        return this;
    }

    public NeuronInitState getNeuronInitState() {
        return neuronInitState;
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

    public LossFunction getLossFunction() {
        return lossFunction;
    }

    /**
     * Sets the loss function which controls the initial error calculation which is propagated back through the network.
     *
     * @param lossFunction the new loss function
     * @return a reference to this object
     */
    public NetworkBuilder setLossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
        checkBuilderViability();
        return this;
    }

    public double getMomentum() {
        return momentum;
    }

    public NetworkBuilder setMomentum(double momentum) {
        this.momentum = momentum;
        checkBuilderViability();
        return this;
    }

    public ActivationFunction getLastLayerFunction() {
        return layerLayerActivationFunction;
    }
}
