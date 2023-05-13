package gk646.jnet.neuralnetwork.builder;

import gk646.jnet.neuralnetwork.exceptions.IllegalNetworkArguments;

import java.util.List;

public final class NetworkBuilder {
    List<Integer> layerInfo;
    ActivationFunction activeFunc;

    NeuronInitState neuronInitState;

    public NetworkBuilder(List<Integer> layerInfo, ActivationFunction activeFunc) {
        this.layerInfo = layerInfo;
        this.activeFunc = activeFunc;

        checkBuilderViability();
    }

    public NetworkBuilder setNeuronInitState(NeuronInitState neuronInitState) {
        this.neuronInitState = neuronInitState;
        return this;
    }

    public short[] getLayerInfo() {
        short[] temp = new short[layerInfo.size()];
        for (int i = 0; i < layerInfo.size(); i++) {
            temp[i] = layerInfo.get(i).shortValue();
        }
        return temp;
    }

    public ActivationFunction getActiveFunc() {
        return activeFunc;
    }

    private void checkBuilderViability() {
        if(layerInfo.size() > 127)  throw new IllegalNetworkArguments("Amount of layers must be less than 128");

        for(int num : layerInfo){
            if(num <= 0){
                throw new IllegalNetworkArguments("Layer size must be greater than 0");
            }else if( num > 127){
                throw new IllegalNetworkArguments("Layer size must be less than 128");
            }
        }

        if(activeFunc == null) throw new IllegalNetworkArguments("No activation function!");
    }
}
