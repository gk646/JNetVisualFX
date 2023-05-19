package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.commands.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public final class Parser {

    static HashMap<String, Method> methodMap = new HashMap<>();
    static HashMap<String, Constructor> constructorMap = new HashMap<>();

    Parser() {
        for (Method method : NetworkBuilder.class.getMethods()) {
            methodMap.put(method.getName(), method);
        }
        for (Method method : Network.class.getMethods()) {
            methodMap.put(method.getName(), method);
        }
        constructorMap.put("Network", NeuralNetwork.class.getConstructors()[0]);
        constructorMap.put("NetBuilder", NetworkBuilder.class.getConstructors()[0]);
    }

    public boolean parse(String text) {
        for (Command command : CommandController.COMMANDS) {
            if (text.startsWith(command.toString())) {
                command.cmd(text);
                return true;
            }
        }
        return parseAliasCommands();
    }

    private boolean parseAliasCommands() {
        return false;
    }

    public static HashMap<String, Method> getMethodMap() {
        return methodMap;
    }

    public static HashMap<String, Constructor> getConstructorMap() {
        return constructorMap;
    }
}
