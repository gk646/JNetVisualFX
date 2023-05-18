package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.commands.ArgCommand;
import gk646.jnet.userinterface.terminal.commands.NoArgCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public final class Parser {

    static HashMap<String, Method> methodMap = new HashMap<>();
    static HashMap<String, Constructor> constructorMap = new HashMap<>();

    public static final ArgCommand[] ARGS_COMMANDS = ArgCommand.values();

    public static final NoArgCommand[] NO_ARGS_COMMANDS = NoArgCommand.values();

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
        if (parseNOARGSCommands(text)) return true;
        return parseARGSCommands(text);
    }

    private boolean parseNOARGSCommands(String text) {
        boolean temp;
        for (NoArgCommand noArgsCmds : NO_ARGS_COMMANDS) {
            temp = noArgsCmds.cmd(text);
            if (temp) {
                return true;
            }
        }
        return false;
    }

    private boolean parseARGSCommands(String prompt) {
        for (ArgCommand argsCommand : ARGS_COMMANDS) {
            if (prompt.startsWith(argsCommand.getKeyWord())) {
                Terminal.addText(argsCommand.cmd(prompt));
                return true;
            }
        }
        return false;
    }

    public static HashMap<String, Method> getMethodMap() {
        return methodMap;
    }

    public static HashMap<String, Constructor> getConstructorMap() {
        return constructorMap;
    }

    public ArgCommand[] getCommands() {
        return ARGS_COMMANDS;
    }
}
