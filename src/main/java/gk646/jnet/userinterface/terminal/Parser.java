package gk646.jnet.userinterface.terminal;

import gk646.jnet.Info;
import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.Window;
import gk646.jnet.userinterface.terminal.commands.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Parser {

    private static final ArrayList<String> exitStringList = new ArrayList<>(List.of("exit", "logout", "shutdown", "goodbye", "bye", "byebye", "cya"));
    static HashMap<String, Method> methodMap = new HashMap<>();
    static HashMap<String, Constructor> constructorMap = new HashMap<>();

    public static final Command[] commands = Command.values();


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
        if (exitStringList.contains(text)) {
            Terminal.clear();
            Terminal.addText("Thanks for using JNetVisualFX! - GoodBye");
            Window.exit();
            return true;
        }
        switch (text) {
            case "clear" -> {
                Terminal.clear();
                return true;
            }
            case "reset" -> {
                Playground.reset();
                Terminal.addText("playground was reset");
                return true;
            }
            case "font" -> {
                Terminal.addText("Cascadia Code : https://github.com/microsoft/cascadia-code");
                return true;
            }
            case "github" -> {
                Terminal.addText("JNetVisualFX's github page: https://github.com/gk646/JNetVisualFX");
                return true;
            }
            case "version" -> {
                Terminal.addText(Info.VERSION);
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
