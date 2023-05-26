package gk646.jnet.userinterface.terminal;

import gk646.jnet.networks.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.util.parser.ArithmeticParser;
import gk646.jnet.util.parser.ArrayParser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class Parser {

    public static final ArithmeticParser numberParser = new ArithmeticParser();
    public static final ArrayParser arrayParser = new ArrayParser();
    static final HashMap<String, Method> methodMap = new HashMap<>();


    Parser() {
        for (Method method : NetworkBuilder.class.getMethods()) {
            methodMap.put(method.getName(), method);
        }
    }


    public static Map<String, Method> getMethodMap() {
        return methodMap;
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
}
