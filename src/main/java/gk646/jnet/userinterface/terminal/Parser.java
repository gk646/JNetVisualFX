package gk646.jnet.userinterface.terminal;

import gk646.jnet.neuralnetwork.Network;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.util.Manual;
import javafx.application.Platform;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public final class Parser {
    HashMap<String, Method> methodMap = new HashMap<>();

    HashMap<String, Constructor> constructorMap = new HashMap<>();


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
        constructorMap.put(Network.class.getSimpleName(), Network.class.getConstructors()[0]);
        constructorMap.put(NetworkBuilder.class.getSimpleName(), NetworkBuilder.class.getConstructors()[0]);
    }


    public boolean parse(String text) {
        if (parseCustomCommands(text)) return true;
        if (text.contains("new")) {
            text = text.replace("new ", "");
            Constructor constructor = constructorMap.get(text);
            if (constructor != null) {
                System.out.println(constructor.getName());
                return true;
            }
        } else {
            Method method = parseMethods(text);
            if (method != null) {
                return true;
            }
        }
        return false;
    }


    private boolean parseCustomCommands(String text) {
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
        if (text.contains("man")) {
            text = text.replace("man ", "");
            Method method = parseMethods(text);
            if (method != null) {
                String annotationText = method.getAnnotation(Manual.class).text().replace("\\*", "");
                Terminal.addText(annotationText);
                return true;
            }
        }
        return false;
    }


    private Method parseMethods(String text) {
        return methodMap.get(text);
    }
}
