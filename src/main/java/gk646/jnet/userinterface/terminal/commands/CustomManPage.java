package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.userinterface.graphics.ColorPalette;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Terminal;

public enum CustomManPage {

    NetworkSize {
        @Override
        public void manual() {
            Terminal.addText("Some problems require a minimal networks size e.g. XOR (2,2,1), because its non linear.");
            Terminal.addText("Choosing the dimensions too big can lead to slower learning and longer waits.");
        }
    },
    themes {
        @Override
        public void manual() {
            for (ColorPalette palette : ColorPalette.values()) {
                Log.addLogText(palette.name());
            }
        }
    },
    ActivationFunction{
        @Override
        public void manual() {
            Terminal.addText("The activation function plays a pivotal role in shaping the behavior of the model, dictating the output form and influencing the learning dynamics.");
            Terminal.addText("SIGMOID function, ranging between 0 and 1, is ideal for binary classification tasks. It smoothly maps input values but can suffer from vanishing gradients during training.");
            Terminal.addText("ReLU function, returning the input value for positive inputs and 0 for negative ones, is computationally efficient and often helps the network learn complex patterns");
            Terminal.addText("TANH function is similar to sigmoid but maps values between -1 and 1. Despite being more computationally intensive, it can offer better performance for certain problems by centering the output.");
            Terminal.addText("LINEAR function just scales the input by a constant factor. While it preserves the proportionality of inputs, it restricts the model from solving non-linear problems effectively.");
        }
    },
    LossFunction{
        @Override
        public void manual() {
            for (ColorPalette palette : ColorPalette.values()) {
                Log.addLogText(palette.name());
            }
        }
    },
    LearnRate{
        @Override
        public void manual() {
            Terminal.addText("Learn rate adjust how much weights are adjusted each step. A high learn rate can lead to faster convergence (solving).");
            Terminal.addText("That is at the risk of overshooting, so its generally better to start lower. Some problems need a very small learn rate e.g. 0.05");
        }
    };

    public abstract void manual();
}
