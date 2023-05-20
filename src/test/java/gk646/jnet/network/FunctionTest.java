package gk646.jnet.network;

import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTest {


    @Test
    void activationFunctionTest() {
        assertEquals(0.5, ActivationFunction.SIGMOID.apply(0));
        assertEquals(3, ActivationFunction.RELU.apply(3));
    }
}
