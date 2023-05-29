package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.Info;
import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.networks.neuralnetwork.NeuralNetwork;
import gk646.jnet.networks.neuralnetwork.builder.ActivationFunction;
import gk646.jnet.networks.neuralnetwork.builder.LossFunction;
import gk646.jnet.userinterface.Window;
import gk646.jnet.userinterface.exercise.Exercise;
import gk646.jnet.userinterface.exercise.ExerciseWindow;
import gk646.jnet.userinterface.graphics.ColorPalette;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.CommandController;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Parser;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.util.NumberUtil;
import gk646.jnet.util.StringUtil;
import gk646.jnet.util.datastructures.Matrix;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Command {

    print("print(<args>) -  prints out the given value, performs basic arithmetic on numbers") {
        private final Pattern pattern = Pattern.compile("print\\((.*?)\\)");

        @Override
        public void cmd(String prompt) {
            if (!prompt.contains(")")) {
                Terminal.addText(missingBracket + prompt);
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
    }, man("man <command/object/property -name> - returns the manual page for that command, object or property") {
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
                    return;
                }
            }
            for (SettableProperties property : SettableProperties.values()) {
                if (property.name().equals(prompt)) {
                    Terminal.addText(property.getManPage());
                    return;
                }
            }
            for (CreatableObjects creatables : CreatableObjects.values()) {
                if (creatables.name().equals(prompt)) {
                    Terminal.addText(creatables.manPage);
                    return;
                }
            }
            for (CustomManPage custom : CustomManPage.values()) {
                if (custom.toString().equals(prompt)) {
                    custom.manual();
                    return;
                }
            }

            Terminal.addText("no object with manual page named: " + prompt);
        }
    }, new1("new <creatableName> - creates a new object; autocompletion shows a list of all creatable objects; can be man<object> to get info") {
        @Override
        public String toString() {
            return "new";
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
                prompt = prompt.replace(creatableName, "");
            }

            for (CreatableObjects creatableObjects : CreatableObjects.values()) {
                if (creatableObjects.name().equals(creatableName)) {
                    creatableObjects.cmd(prompt);
                    return;
                }
            }
            Terminal.addText("no creatable object named: " + creatableName);
        }
    },

    exit("exit - exits the application") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
            Terminal.addText("Thanks for using JNetVisualFX! - GoodBye");
            Window.exit();
        }
    }, hello("hello - greets the user") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Hello - good to see you!");
        }
    }, clear("clear - clears the whole text history for only the terminal") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
        }
    }, font("font - info about the font") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Cascadia Code : https://github.com/microsoft/cascadia-code");
        }
    }, github("github - github link to the project ; give me a star if you like it ;)") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("JNetVisualFX's github page: https://github.com/gk646/JNetVisualFX");
        }
    }, version("version - version info") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText(Info.VERSION);
        }
    },

    resetall("resetall - resets all properties to default value; includes the \"reset\" command") {
        @Override
        public void cmd(String prompt) {
            NetworkVisualizer.maxCircleDiameter = 30;
            SettableProperties.fontsize.cmd("15");
            Terminal.terminalRoot = ">";
            Terminal.addText("reset all value to default state!");
            ColorPalette.DEFAULT.loadPalette();
        }
    }, helpall("helpall -  lists all commands including their man page in the log") {
        @Override
        public void cmd(String prompt) {
            for (Command command : CommandController.COMMANDS) {
                Log.addLogText(command.getManPage());
            }
        }
    }, help("help - your") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("man <method or command> for manual the manual page");
            Terminal.addText("if you need more help open the the JNetVisualFX wiki with: \"wiki\"");
        }
    },
    wiki("wiki - opens the JNetVisualFX's github wiki pages") {
        private static final String wiki = "https://github.com/gk646/JNetVisualFX/wiki";

        @Override
        public void cmd(String prompt) {
            try {
                Desktop.getDesktop().browse(URI.create(wiki));
            } catch (IOException e) {
                Terminal.addText("error opening: " + wiki);
            }
        }
    }, name("name <name> - name yourself ; adds your name to the terminal root; \"name reset\" to reset") {
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
    }, quicksetup("quicksetup - helper command; not to be used") {
        @Override
        public void cmd(String prompt) {
            Terminal.parseText("new NetBuilder([2,2,1],sigmoid)");
            Terminal.parseText("new Network");
            Terminal.parseText("list he [23,23,23]");
            Terminal.parseText("list hey [23,23,24]");

            Terminal.parseText("list input [[0,1][1,0][0,0][1,1]]");
            Terminal.parseText("list output [[1],[1],[0],[0]]");

            Terminal.parseText("jnet_train(input,output,3)");
        }
    }, listget("listget <list-name> - replaces the commandline with the correct syntax to update the given list") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("listget ", "");
            if (prompt.isBlank() || prompt.equals("listget")) {
                Terminal.addText("missing list name: " + this + " <list-name>");
                return;
            }
            Matrix matrix = Playground.playgroundLists.get(prompt);
            if (matrix != null) {
                Terminal.currentText.setLength(0);
                Terminal.currentText.append("list ").append(prompt).append(" ").append(matrix);
            } else {
                Terminal.addText("no list named: " + prompt);
            }
        }
    }, listall("listall - displays all named lists; use \"list\" to create one") {
        @Override
        public void cmd(String prompt) {
            if (Playground.playgroundLists.isEmpty()) {
                Terminal.addText("no saved lists");
                return;
            }
            Playground.playgroundLists.forEach((s, matrix) -> Terminal.addText(s + ": " + matrix));
        }
    }, list("list <name> <[[list,of][num,bers]]> - creates and saves a named list out of the given input; can be floating points ; max depth is 2 ; bracket type = []") {
        static final String syntax = " <name> <[array type]>";

        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("list ", "");
            if (prompt.isBlank() || prompt.equals("list")) {
                Terminal.addText("missing list name: " + this + syntax);
                return;
            }

            String listName;
            if (!prompt.contains(" ")) {
                Terminal.addText("missing whitespace: " + this + syntax);
                return;
            }
            listName = prompt.substring(0, prompt.indexOf(" "));


            prompt = prompt.replace(listName + " ", "");
            if (prompt.isBlank()) {
                Terminal.addText("missing array declaration: " + this + syntax);
                return;
            }
            Parser.arrayParser.parseList(prompt, listName);
        }
    }, $("$<variable-Name> - denotes a variable") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.substring(1);
            Matrix matrix = Playground.playgroundLists.get(prompt);
            if (matrix != null) {
                Terminal.addText(matrix.toString());
            } else {
                Terminal.addText("no variable named: " + prompt);
            }
        }
    }, reset_user_statistics("reset_user_statistics - resets the user statistics completely; only necessary when user-statistics.txt is unreadable ;USE WITH CARE") {
        @Override
        public void cmd(String prompt) {
            Window.localFileSaver.resetUserStatistic();
        }
    }, reset("reset - reset the playground // reset both the NetBuilder and current Network to null") {
        @Override
        public void cmd(String prompt) {
            Playground.reset();
            Terminal.addText("playground was reset");
        }
    },

    getStat("getStat <stat-name> - retrieves a statistic; returns the most updated value") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("getStat ", "");
            if (prompt.isEmpty() || prompt.equals("getStat")) {
                Terminal.addText("Missing argument: getStat <stat-name>");
                return;
            }

            UserStatistics.Stat stat;
            try {
                stat = UserStatistics.Stat.valueOf(prompt);
            } catch (IllegalArgumentException ignored) {
                Terminal.addText("no statistic with such name");
                return;
            }
            if (stat == UserStatistics.Stat.totalSecondsUsed) {
                Terminal.addText(String.valueOf(NumberUtil.getNewTotalTime(UserStatistics.getStat(stat))));
                return;
            }


            Terminal.addText(UserStatistics.getStat(stat).toString());
        }
    },

    jnet_train("jnet-train(<input-list>,<target-list>,<repetitions>) - trains your active Network; needs data to be in the same shape") {
        static final String syntax = "(<input-list>,<target-list>,<repetitions>)";

        @Override
        public void cmd(String prompt) {
            if (noNetworkCheck()) return;

            prompt = prompt.replace("jnet_train(", "");
            if (prompt.isBlank() || prompt.equals("jnet_train")) {
                Terminal.addText(missingList + "jnet_train(" + syntax);
                return;
            }
            if (!prompt.contains(")")) {
                Terminal.addText(missingBracket + this + syntax);
                return;
            }
            if (StringUtil.countChar(prompt, ',') != 2) {
                Terminal.addText("missing commas/arguments: " + this + syntax);
                return;
            }

            String[] arguments = prompt.substring(0, prompt.length() - 1).split(",");


            Matrix input = Playground.playgroundLists.get(arguments[0]);
            Matrix target = Playground.playgroundLists.get(arguments[1]);

            if (input == null) {
                Terminal.addText("input matrix not fond: " + arguments[0]);
                return;
            }
            if (target == null) {
                Terminal.addText("target matrix not found: " + arguments[1]);
                return;
            }

            int repetitions;
            try {
                repetitions = Integer.parseInt(arguments[2]);
            } catch (NumberFormatException e) {
                Terminal.addText("repetitions must be a valid integer: " + arguments[2]);
                return;
            }

            if (NeuralNetwork.worker != null) {
                Log.logger.severe("training already in progress! \"jnet_fastforward\" to skip");
                return;
            }

            Playground.neuralNetwork.trainVisual(input.getRawData(), target.getRawData(), repetitions);
            Terminal.addText("training started! \"jnet_fastforward\" to skip delays; \"set trainDelay <value>\" to change it");
            Log.addLogText("*------NEW TRAINING ---- " + repetitions + " REPETITIONS--------*");
        }
    },
    jnet_randomtrain("jnet-randomtrain(<input-list>,<target-list>,<repetitions>) - randomly shuffles training data ; same usage as jnet-train") {
        static final String syntax = "(<input-list>,<target-list>,<repetitions>)";

        @Override
        public void cmd(String prompt) {
            if (noNetworkCheck()) return;

            prompt = prompt.replace("jnet_randomtrain(", "");
            if (prompt.isBlank() || prompt.equals("jnet_randomtrain")) {
                Terminal.addText(missingList + this + syntax);
                return;
            }
            if (!prompt.contains(")")) {
                Terminal.addText(missingBracket + this + syntax);
                return;
            }
            if (StringUtil.countChar(prompt, ',') != 2) {
                Terminal.addText("missing commas/arguments: " + this + syntax);
                return;
            }

            String[] arguments = prompt.substring(0, prompt.length() - 1).split(",");


            Matrix input = Playground.playgroundLists.get(arguments[0]);
            Matrix target = Playground.playgroundLists.get(arguments[1]);

            if (input == null) {
                Terminal.addText("input matrix not fond: " + arguments[0]);
                return;
            }
            if (target == null) {
                Terminal.addText("target matrix not found: " + arguments[1]);
                return;
            }

            int repetitions;
            try {
                repetitions = Integer.parseInt(arguments[2]);
            } catch (NumberFormatException e) {
                Terminal.addText("repetitions must be a valid integer: " + arguments[2]);
                return;
            }

            Playground.neuralNetwork.trainRandom(input.getRawData(), target.getRawData(), repetitions);
        }
    },
    jnet_out("jnet-out(<input-list / [array] >) - gives you the output for a given input ; accepts both a listname or new declared list") {
        static final String syntax = "(<input-list>)";

        @Override
        public void cmd(String prompt) {
            if (noNetworkCheck()) return;

            prompt = prompt.replace("jnet_out(", "");
            if (prompt.isBlank() || prompt.equals("jnet_out")) {
                Terminal.addText(missingList + this + syntax);
                return;
            }
            if (!prompt.contains(")")) {
                Terminal.addText(missingBracket + this + syntax);
                return;
            }

            if (prompt.contains("[")) {
                double[] input;
                try {
                    input = Parser.arrayParser.parseArrayFromString(prompt.substring(1, prompt.length() - 2));
                } catch (NumberFormatException ignored) {
                    Terminal.addText("invalid list declaration");
                    return;
                }
                Terminal.addText(new Matrix((Playground.neuralNetwork.out(input))).toString());
            } else {
                Matrix input = Playground.playgroundLists.get(prompt.substring(0, prompt.length() - 1));
                if (input == null) {
                    Terminal.addText("input matrix not fond: " + prompt.substring(0, prompt.length() - 1));
                    return;
                }
                Terminal.addText(new Matrix((Playground.neuralNetwork.out(input.getRow(0)))).toString());
            }
        }
    },

    jnet_fastforward("jnet_fastforward - will skip all delays to quickly finish the task") {
        @Override
        public void cmd(String prompt) {
            NeuralNetwork.resetWorker();
        }
    },
    theme("theme <theme-name> - set the active theme; \"man themes\" to get a list of all themes") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("theme ", "");
            if (prompt.isBlank() || prompt.equals("theme")) {
                Terminal.addText("missing theme name");
                return;
            }

            for (ColorPalette palette : ColorPalette.values()) {
                if (palette.name().equals(prompt)) {
                    palette.loadPalette();
                    return;
                }
            }
            Terminal.addText("no theme named: " + prompt);
        }
    },
    setLearnRate("setLearnRate <floating-point> - \"man LearnRate\" for info ; sets the builders learn rate") {
        @Override
        public void cmd(String prompt) {
            if (noNetBuilderCheck()) return;

            prompt = prompt.replace("setLearnRate ", "");
            if (prompt.isBlank() || prompt.equals(this.name())) {
                Terminal.addText(missingArgument);
                return;
            }

            double parsedNum;
            try {
                parsedNum = Double.parseDouble(prompt);
            } catch (NumberFormatException ignored) {
                Terminal.addText("couldnt parse " + this + " <" + this + ">: " + prompt);
                return;
            }

            if (parsedNum > 0 && parsedNum < 10) {
                Playground.networkBuilder.setLearnRate(parsedNum);
                Terminal.addText("set new " + this + ": " + parsedNum);
                return;
            }
            Terminal.addText("invalid " + this + ": " + parsedNum);
        }
    },

    setNetworkSize("setNetworkSize <[list]> - sets the NetBuilder network size ; \"man NetworkSize\" for info") {
        @Override
        public void cmd(String prompt) {
            if (noNetBuilderCheck()) return;

            prompt = prompt.replace("setNetworkSize ", "");
            if (prompt.isBlank() || prompt.equals(this.name())) {
                Terminal.addText(missingArgument);
                return;
            }

            int[] newLayerInfo;
            try {
                newLayerInfo = Parser.arrayParser.parseIntArrayFromString(prompt.substring(1, prompt.length() - 1));
            } catch (NumberFormatException ignored) {
                Terminal.addText("wrong array declaration");
                return;
            }

            Playground.networkBuilder.setNetworkSize(newLayerInfo);
            Terminal.addText("set new network size: " + Arrays.toString(newLayerInfo));
        }
    },
    setActivationFunction("setActivationFunction <activation-func-name> - \"man ActivationFunction\" for a list ; sets the NetBuilder activation function") {
        @Override
        public void cmd(String prompt) {
            if (noNetBuilderCheck()) return;

            prompt = prompt.replace(this + " ", "");
            if (prompt.isBlank() || prompt.equals(this.name())) {
                Terminal.addText(missingArgument);
                return;
            }
            ActivationFunction activeFunc;
            try {
                activeFunc = ActivationFunction.valueOf(prompt.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                Terminal.addText("no activation function named: " + prompt);
                return;
            }
            Playground.networkBuilder.setActivationFunction(activeFunc);
            Terminal.addText("set new activation function: " + activeFunc);
        }
    },
    setLossFunction("setLossFunction <loss-func-name> - \"man LossFunction\" for a list ; sets the NetBuilder loss function") {
        @Override
        public void cmd(String prompt) {
            if (noNetBuilderCheck()) return;

            prompt = prompt.replace(this + " ", "");
            if (prompt.isBlank() || prompt.equals(this.name())) {
                Terminal.addText(missingArgument);
                return;
            }

            LossFunction lossFunc;
            try {
                lossFunc = LossFunction.valueOf(prompt.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                Terminal.addText("no loss function named: " + prompt);
                return;
            }
            Playground.networkBuilder.setLossFunction(lossFunc);
            Terminal.addText("set new loss function: " + lossFunc);
        }
    },
    set("set <propertyName>(<value>) - sets the given property; autocompletion shows list ; properties can be man<name> to get info") {
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

            for (SettableProperties settableProperties : SettableProperties.values()) {
                if (settableProperties.name().equals(creatableName)) {
                    Terminal.addText(settableProperties.cmd(prompt));
                    return;
                }
            }
            Terminal.addText("no property named:" + creatableName);
        }
    },

    exercise("exercise <number> - opens a new windows containing information about the exercise") {
        @Override
        public void cmd(String prompt) {
            prompt = prompt.replace("exercise ", "");
            if (prompt.isBlank() || prompt.equals("exercise")) {
                Terminal.addText("missing argument: exercise <number>");
                return;
            }

            for (Exercise exercise : Exercise.values()) {
                if (exercise.toString().equals(prompt)) {
                    ExerciseWindow.create(exercise);
                    Terminal.addText("You can close and reopen the exercise window any time you want.\"ex_test\" to test if you finished the exercise, \"ex_hint\" to get a helpful hint.");
                    return;
                }
            }
            Terminal.addText("no exercise with number: " + prompt);
        }
    },
    ex_test("ex_test - test the active network against the active exercise") {
        @Override
        public void cmd(String prompt) {
            if (noNetworkCheck()) return;
            if (noExerciseCheck()) return;
            ExerciseWindow.test();
        }
    },
    ex_hint("ex_hint - give you a hint to the current exercise; resets each time you reopen one") {
        @Override
        public void cmd(String prompt) {
            if (noNetworkCheck()) return;
            if (noExerciseCheck()) return;
            ExerciseWindow.getHint();
        }
    };


    static final String missingBracket = "missing closing bracket: ";
    static final String noNetwork = "no neural network!";
    static final String missingList = "missing list names: ";
    static final String noNetBuilder = "no NetBuilder! \"man NetBuilder\" for info";
    static final String missingArgument = "missing argument";
    static final String noExercise = "no active exercise!";
    private final String manPage;
    Matcher matcher;


    Command(String manPage) {
        this.manPage = manPage;
    }

    public abstract void cmd(String prompt);

    public String getManPage() {
        return manPage;
    }

    boolean noNetBuilderCheck() {
        if (Playground.networkBuilder == null) {
            Terminal.addText(noNetBuilder);
            return true;
        }
        return false;
    }

    boolean noExerciseCheck() {
        if (!ExerciseWindow.isOpen()) {
            Terminal.addText(noExercise);
            return true;
        }
        return false;
    }

    boolean noNetworkCheck() {
        if (Playground.neuralNetwork == null) {
            Terminal.addText(noNetwork);
            return true;
        }
        return false;
    }
}