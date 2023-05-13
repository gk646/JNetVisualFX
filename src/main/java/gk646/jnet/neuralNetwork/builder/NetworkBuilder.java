package gk646.jnet.neuralNetwork.builder;

import java.util.List;

public final class NetworkBuilder {
    List<Integer> layerInfo;
    ActivationFunction activeFunc;

    NeuronInitState neuronInitState;

    public NetworkBuilder(List<Integer> layerInfo, ActivationFunction activeFunc) {
        this.layerInfo = layerInfo;
        this.activeFunc = activeFunc;
    }


    public NetworkBuilder setNeuronInitState(NeuronInitState neuronInitState) {
        this.neuronInitState = neuronInitState;
        return this;
    }

    public List<Integer> getLayerInfo() {
        return layerInfo;
    }

    public ActivationFunction getActiveFunc() {
        return activeFunc;
    }
}
