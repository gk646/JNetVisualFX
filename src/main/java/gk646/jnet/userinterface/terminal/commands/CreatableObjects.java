package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.networks.neuralnetwork.NeuralNetwork;
import gk646.jnet.networks.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.Parser;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.util.NumberUtil;
import gk646.jnet.util.datastructures.Matrix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CreatableObjects {

    NetBuilder("") {
        static final Pattern listPattern = Pattern.compile("(\\[[^]]*+]|\\$\\w+)\\s*,\\s*(\\w+)");

        @Override

        public void cmd(String prompt) {
            if (prompt.isBlank() || prompt.equals("(")) {
                Terminal.addText("missing arguments: new NetBuilder([<List of numbers>],<activationFunction>)");
                return;
            }

            Matcher matcher = listPattern.matcher(prompt);

            if (!matcher.find()) {
                Terminal.addText("invalid syntax: (<$listname | [<list>]>, <activationFunction>)");
                return;
            }

            String listOrVariableName = matcher.group(1);
            String functionArgument = matcher.group(2);

            int[] layerInfo;

            if (listOrVariableName.contains("$")) {
                String listName = listOrVariableName.substring(1);
                Matrix matrix = Playground.playgroundLists.get(listName);
                if (matrix == null || matrix.isEmpty()) {
                    Terminal.addText("given list is empty: " + listName);
                    return;
                }
                layerInfo = NumberUtil.arrayToInt(matrix.getRow(0));
            } else {
                layerInfo = Parser.arrayParser.parseIntArrayFromString(listOrVariableName.substring(1, listOrVariableName.length() - 1));
            }

            ActivationFunction activeFunction;
            try {
                activeFunction = ActivationFunction.valueOf(functionArgument.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                Terminal.addText("no activationFunction named: " + functionArgument);
                return;
            }

            Playground.networkBuilder = new NetworkBuilder(layerInfo, activeFunction);
            Terminal.addText("created new NetBuilder");
        }
    }, Network("") {
        public void cmd(String prompt) {
            if (Playground.networkBuilder != null) {
                Playground.neuralNetwork = new NeuralNetwork(Playground.networkBuilder);
                Terminal.addText("created new NeuralNetwork with the active NetBuilder");
                return;
            }
            Terminal.addText("missing NetBuilder; create NetBuilder first ; for help: man NetBuilder");
        }
    };


    final String manPage;

    CreatableObjects(String manPage) {
        this.manPage = manPage;
    }

    public abstract void cmd(String prompt);
}
