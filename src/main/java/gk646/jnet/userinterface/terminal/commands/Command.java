package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.Parser;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.Manual;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {

    PRINT(Pattern.compile("print\\((.*?)\\)"), "print", "print - terminal output // prints out the given value, performs basic arithmetic on numbers // Syntax - print(<args>)") {
        @Override
        public String cmd(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return "Missing argument: print(<args>)";
        }
    },
    MAN(Pattern.compile("man"), "man", "man - manual // returns the manual page for that command or method //Syntax - man <commandName>") {
        @Override
        public String cmd(String prompt) {
            prompt = prompt.replace("man ", "");
            if (prompt.isBlank()) {
                return "Missing argument: man <methodName> ";
            }
            Method method = Parser.getMethodMap().get(prompt);
            if (method != null) {
                Manual manual = method.getAnnotation(Manual.class);
                if (manual != null) {
                    return manual.text().replace("\\*", "");
                }
            }
            Constructor constructor = Parser.getConstructorMap().get(prompt);
            if (constructor != null) {
                Manual manual = (Manual) constructor.getAnnotation(Manual.class);
                if (manual != null) {
                    return manual.text().replace("\\*", "");
                }
            }
            for (Command command : Parser.commands) {
                if (command.keyWord.equals(prompt)) {
                    return command.manPage;
                }
            }
            return "no object with manual page named: " + prompt;
        }
    },
    HELP(Pattern.compile("help"), "help", "help - how to use the termianl // returns a help page that allows show how to get information about other components // Syntax - help") {
        @Override
        public String cmd(String prompt) {
            return "man <method or command> for manual the manual page";
        }
    },
    NEW(Pattern.compile("(\\w+)\\(\\(([^)]+)\\),([^)]+)\\)"), "new", "new - create new object // works with either \"Network\" or \"NetBuilder\" // Syntax - new <ObjectName>") {
        private boolean invalidDimensions(List<Integer> arr) {
            for (int num : arr) {
                if (num <= 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String cmd(String prompt) {
            prompt = prompt.replace("new ", "");
            if (prompt.isBlank() || prompt.equals("new")) {
                return "missing argument: new <creatableObject>  || possible: \"Network\",\"NetBuilder\"";
            }
            String creatableName = prompt;
            if (prompt.contains("(")) {
                creatableName = prompt.substring(0, prompt.indexOf("("));
            }

            switch (creatableName) {
                case "NetBuilder" -> {
                    return netBuilderConstructor(prompt);
                }
                case "Network" -> {
                    if (Playground.networkBuilder != null) {
                        Playground.neuralNetwork = new NeuralNetwork(Playground.networkBuilder);
                        return "created a NeuralNetwork with the active NetBuilder";
                    }
                    return "missing NetBuilder || create a NetBuilder first || for help: man NetBuilder";
                }
            }
            return "no creatable object named: " + prompt;
        }

        private String netBuilderConstructor(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                String functionName = matcher.group(1);  // "Network"

                Constructor constructor = Parser.getConstructorMap().get(functionName);
                if (constructor == null) {
                    return "no creatable object named: " + functionName;
                }

                String[] numbers = matcher.group(2).split(",");  // ["1", "2", "3"]
                List<Integer> numList = new ArrayList<>();
                for (String number : numbers) {
                    numList.add(Integer.parseInt(number));
                }

                if (numbers.length == 0 || invalidDimensions(numList)) {
                    return "networkDimensions are illegal // (4,4,4) -> creates a network with 3 layers and 4 neurons each";
                }

                String activationFunctionString = matcher.group(3).toUpperCase();  // "SIGMOID"

                ActivationFunction activeFunction;
                try {
                    activeFunction = ActivationFunction.valueOf(activationFunctionString);
                } catch (IllegalArgumentException ignored) {
                    return "no activationFunction named: " + activationFunctionString;
                }

                try {
                    Playground.networkBuilder = (NetworkBuilder) constructor.newInstance(numList, activeFunction);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    return "error creating new object: " + e.getMessage();
                }

                return "Created new " + functionName;
            }
            return "Missing arguments: new NetBuiler((<List of numbers>),<activationFunction>)  || e.g NetBuilder((3,3,3),sigmoid)";
        }
    };
    Matcher matcher;
    Pattern pattern;
    String keyWord;
    String manPage;

    Command(Pattern pattern, String keyWord, String manPage) {
        this.pattern = pattern;
        this.keyWord = keyWord;
        this.manPage = manPage;
    }

    public abstract String cmd(String prompt);

    public String getKeyWord() {
        return keyWord;
    }


    private boolean missingArgumentCheck(String prompt) {
        prompt = prompt.replace(keyWord, "");
        if (prompt.isBlank()) {
            //return "Missing argument: man <methodName> ";
        }
        return true;
    }
}
