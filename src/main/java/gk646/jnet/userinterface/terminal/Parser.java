package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.commands.Command;
import javafx.application.Platform;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;

public final class Parser {

    Matcher matcher;
    static HashMap<String, Method> methodMap = new HashMap<>();

    static HashMap<String, Constructor> constructorMap = new HashMap<>();

    public static Command[] commands = Command.values();


    Parser() {
        for (Method method : NetworkBuilder.class.getMethods()) {
            methodMap.put(method.getName(), method);
        }
        for (Constructor constructor : NetworkBuilder.class.getConstructors()) {
            constructorMap.put(constructor.getName(), constructor);
        }

        for (Method method : Network.class.getMethods()) {
            methodMap.put(method.getName(), method);
        }
        constructorMap.put(Network.class.getSimpleName(), NeuralNetwork.class.getConstructors()[0]);
        constructorMap.put("NetBuilder", NetworkBuilder.class.getConstructors()[0]);
    }


    public boolean parse(String text) {
        if (parseNOARGSCommands(text)) return true;
        if (parseARGSCommands(text)) return true;

        if (text.contains("new")) {
            text = text.replace("new ", "");
            Constructor constructor = constructorMap.get(text);
            if (constructor != null) {
                System.out.println(constructor.getName());
                return true;
            }
        } else {
            Method method = methodMap.get(text);
            if (method != null) {
                //TODO invoke methods
                return true;
            }
        }
        return false;
    }


    private boolean parseNOARGSCommands(String text) {
        switch (text) {
            case "clear" -> {
                Terminal.clear();
                return true;
            }
            case "shutdown" -> {
                Platform.exit();
                return true;
            }
            case "delete" -> {
                Playground.networkBuilder = null;
                Playground.neuralNetwork = null;
                return true;
            }
        }
        return false;
    }

    private boolean parseARGSCommands(String prompt) {
        for (Command command : commands) {
            if (prompt.startsWith(command.getKeyWord())) {
                Terminal.addText(command.cmd(prompt));
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

    public Command[] getCommands() {
        return commands;
    }
}
