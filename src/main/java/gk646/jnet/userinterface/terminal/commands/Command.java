package gk646.jnet.userinterface.terminal.commands;

import com.sun.javafx.geom.Matrix3f;
import gk646.jnet.Info;
import gk646.jnet.neuralnetwork.NeuralNetwork;
import gk646.jnet.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.neuralnetwork.builder.NetworkBuilder;
import gk646.jnet.userinterface.Window;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.CommandController;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Parser;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.util.datastructures.Matrix;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {

    print("print - terminal output // prints out the given value, performs basic arithmetic on numbers // Syntax - print(<args>)") {
        private final Pattern pattern = Pattern.compile("print\\((.*?)\\)");

        @Override
        public void cmd(String prompt) {
            if (!prompt.contains(")")) {
                Terminal.addText("missing closing bracket: " + prompt);
                return;
            }

            if (Parser.numberParser.parse(prompt)) return;

            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                Terminal.addText(matcher.group(1));
                return;
            }
            Terminal.addText("Missing argument: print(<args>)");
        }
    },
    man("man - manual // returns the manual page for that command or method //Syntax - man <commandName>") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("man ", "");
            if (prompt.isBlank()) {
                Terminal.addText("Missing argument: man <methodName> ");
                return;
            }
            for (Command command : CommandController.COMMANDS) {
                if (command.toString().equals(prompt)) {
                    Terminal.addText(command.manPage);
                }
            }
            for (SettableProperties property : CommandController.SETTABLE_PROPERTIES) {
                if (property.name().equals(prompt)) {
                    Terminal.addText(property.getManPage());
                    return;
                }
            }
            Terminal.addText("no object with manual page named: " + prompt);
        }
    },
    new1("new - create new object // works with either \"Network\" or \"NetBuilder\" // Syntax - new <ObjectName>") {

        private final Pattern pattern = Pattern.compile("(\\w+)\\(\\[([^)]+)],([^)]+)\\)");

        @Override
        public String toString() {
            return "new";
        }

        private boolean invalidDimensions(List<Integer> arr) {
            for (int num : arr) {
                if (num <= 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("new ", "");
            if (prompt.isBlank() || prompt.equals("new")) {
                Terminal.addText("missing argument: new <creatableObject>  || possible: \"Network\",\"NetBuilder\"");
                return;
            }
            String creatableName = prompt;
            if (prompt.contains("(")) {
                creatableName = prompt.substring(0, prompt.indexOf("("));
            }

            switch (creatableName) {
                case "NetBuilder" -> {
                    Terminal.addText(netBuilderConstructor(prompt));
                    return;
                }
                case "Network" -> {
                    if (Playground.networkBuilder != null) {
                        Playground.neuralNetwork = new NeuralNetwork(Playground.networkBuilder);
                        Terminal.addText("created a NeuralNetwork with the active NetBuilder");
                        return;
                    }
                    Terminal.addText("missing NetBuilder || create a NetBuilder first || for help: man NetBuilder");
                    return;
                }
            }
            Terminal.addText("no creatable object named: " + prompt);
        }

        private String netBuilderConstructor(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                String functionName = matcher.group(1);  // "Network"

                Constructor constructor = Parser.getConstructorMap().get(functionName);
                if (constructor == null) {
                    return "no creatable object named: " + functionName;
                }

                String[] numbers = matcher.group(2).split(",");
                List<Integer> numList = new ArrayList<>();
                for (String number : numbers) {
                    numList.add(Integer.parseInt(number));
                }

                if (numbers.length == 0 || invalidDimensions(numList)) {
                    return "networkDimensions are illegal // [4,4,4] -> creates a network with 3 layers and 4 neurons each";
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

                return "created new " + functionName;
            }
            return "Missing arguments: new NetBuilder([<List of numbers>],<activationFunction>)  || e.g NetBuilder([3,3,3],sigmoid)";
        }
    },
    set("set - sets a given property // sets properties e.g circlesize, fontsize, bgr_color... // Syntax - set <propertyName>(<value>)") {
        static final SettableProperties[] SETTABLE_PROPERTIES = SettableProperties.values();

        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("set ", "");
            if (prompt.isBlank() || prompt.equals("set")) {
                Terminal.addText("missing argument: set <propertyName> value || e.g. \"set circlesize 25\"");
                return;
            }
            String creatableName = prompt;
            if (prompt.contains(" ")) {
                creatableName = prompt.substring(0, prompt.indexOf(" "));
                prompt = prompt.replace(creatableName, "").trim();
            }

            for (SettableProperties settableProperties : SETTABLE_PROPERTIES) {
                if (settableProperties.name().equals(creatableName)) {
                    Terminal.addText(settableProperties.cmd(prompt));
                    return;
                }
            }
            Terminal.addText("no property named:" + creatableName);
        }
    },
    exit("exit - exits the application // // Syntax: <any exit word>") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
            Terminal.addText("Thanks for using JNetVisualFX! - GoodBye");
            Window.exit();
        }
    },
    hello("hello - greets the user // // Syntax: <any hello word>") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Hello - good to see you!");
        }
    },
    clear("clear - clears the terminal // clears the whole text history for only the terminal // Syntax: clear") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
        }
    },
    font("font - info about the font // // Syntax: font") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Cascadia Code : https://github.com/microsoft/cascadia-code");
        }
    },
    github("github - github link to the project // give me a star if you like it ;) // Syntax: github") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("JNetVisualFX's github page: https://github.com/gk646/JNetVisualFX");
        }
    },
    version("version - version info // // Syntax: version") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText(Info.VERSION);
        }
    },
    resetall("resetall - resets all properties to default value // // Syntax: resetall") {
        @Override
        public void cmd(String prompt) {
            NetworkVisualizer.maxCircleDiameter = 20;
            SettableProperties.fontsize.cmd("(15)");
            Terminal.terminalRoot = ">";
            Terminal.addText("reset all value to default state!");
        }
    },
    helpall("helpall - lists all commands // lists all commands including their man page // Syntax: helpall") {
        @Override
        public void cmd(String prompt) {
            for (Command command : CommandController.COMMANDS) {
                Log.addLogText(command.getManPage());
            }
        }
    },
    help("") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("man <method or command> for manual the manual page");
        }
    },
    reset("reset - reset the playground // reset both the NetBuilder and current Network to null // Syntax: reset") {
        @Override
        public void cmd(String prompt) {

            Playground.reset();
            Terminal.addText("playground was reset");
        }
    },
    wiki("wiki - opens the wiki // opens the github wiki page about JNetVisualFX // Syntax: wiki") {
        private static final String wiki = "https://github.com/gk646/JNetVisualFX/wiki";

        @Override
        public void cmd(String prompt) {
            try {
                Desktop.getDesktop().browse(URI.create(wiki));
            } catch (IOException e) {
                Terminal.addText("error opening: " + wiki);
            }
        }
    },
    name("name - name yourself // adds your name to the terminal root; \"reset\" to reset // Syntax: name <name>") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("name ", "");
            if (prompt.isEmpty()) {
                Terminal.addText("Missing argument: name <name>");

                return;
            }
            if (prompt.equals("reset")) {
                Terminal.terminalRoot = ">";
                return;
            }
            Terminal.terminalRoot = prompt + ">";
        }
    },
    quicksetup("quicksetup - quickly generates a basic Network((4,4,4), sigmoid)// Syntax: quicksetup") {
        @Override
        public void cmd(String prompt) {
            Terminal.parseText("new NetBuilder([4,4,4],sigmoid)");
            Terminal.parseText("new Network");
        }
    },
    listall("listall - displays a list of lists created ") {
        @Override
        public void cmd(String prompt) {
            Playground.playgroundLists.put("he", Matrix.fromArray(new double[][]{{2},{2}}));
            Playground.playgroundLists.forEach((s, matrix) -> Terminal.addText(s + ": " + matrix));
        }
    },
    list("list - creates a list out of the given input; can be floating points ; max depth is 2 ; any bracket type // Syntax: list <name> <[list ( of [ numbers })}> ") {
        static final String syntax = " <name> <[array type]>";

        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("list ", "");
            if (prompt.isBlank() || prompt.equals("list")) {
                Terminal.addText("missing argument: " + this + syntax);
                return;
            }

            String listName;
            listName = prompt.substring(0, prompt.indexOf(" "));

            if (listName.isBlank()) {
                Terminal.addText("missing list name: " + this + syntax);
                return;
            }

            prompt = prompt.replace(listName + " ", "");
            if (prompt.isBlank()) {
                Terminal.addText("missing array declaration: " + this + syntax);
                return;
            }
            Parser.arrayParser.parseList(prompt, listName);
        }
    };

    Matcher matcher;
    private final String manPage;

    Command(String manPage) {
        this.manPage = manPage;
    }

    public abstract void cmd(String prompt);

    public String getManPage() {
        return manPage;
    }
}
