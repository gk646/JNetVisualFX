package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.networks.neuralnetwork.NeuralNetwork;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.Terminal;

import java.util.function.Consumer;

public enum SettableProperties {

    circlesize("settable property with: set <propertyName> <value> ; maximum pixel size of the neurons: 0 - 100, Default: 20") {
        @Override
        public String cmd(String prompt) {
            return parseSetNumber(0, 100, number -> NetworkVisualizer.maxCircleDiameter = number, prompt);
        }
    },
    fontsize("settable property with: set <propertyName> <value> ; fontsize of the terminal font: 0 - 50, Default: 15") {
        @Override
        public String cmd(String prompt) {
            return parseSetNumber(0, 50, number -> Terminal.TerminalInfo.changeFontSize(number - Terminal.TerminalInfo.getFontSize()), prompt);
        }
    }, trainDelay("settable property with: set <propertyName> <value> ; delay in milli seconds per calculation while training: 0 - 1500, Default: 64") {
        @Override
        public String cmd(String prompt) {
            return parseSetNumber(0, 1500, NeuralNetwork::setDelayPerStep, prompt);
        }
    };
    private final String manPage;

    SettableProperties(String manPage) {
        this.manPage = manPage;
    }

    public abstract String cmd(String prompt);

    public String getManPage() {
        return manPage;
    }

    String parseSetNumber(int origin, int bound, Consumer<Integer> action, String prompt) {
        int parsedNum;
        try {
            parsedNum = Integer.parseInt(prompt);
        } catch (NumberFormatException ignored) {
            return "couldnt parse " + this + " <" + this + ">: " + prompt;
        }

        if (parsedNum > origin && parsedNum < bound) {
            action.accept(parsedNum);
            return "set new " + this + ": " + parsedNum;
        }
        return "invalid " + this + ": " + parsedNum;
    }
}
