package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.Info;
import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.Window;
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
    }, man("man - manual // returns the manual page for that command or method //Syntax - man <commandName>") {
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
            for (SettableProperties property : CommandController.SETTABLE_PROPERTIES) {
                if (property.name().equals(prompt)) {
                    Terminal.addText(property.getManPage());
                    return;
                }
            }
            Terminal.addText("no object with manual page named: " + prompt);
        }
    }, new1("new - create new object // works with either \"Network\" or \"NetBuilder\" // Syntax - new <ObjectName>") {

        private final Pattern pattern = Pattern.compile("(\\w+)\\(\\[([^)]+)],([^)]+)\\)");

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

            for (CreatableObjects creatableObjects : CommandController.CREATABLE_OBJECTS) {
                if (creatableObjects.name().equals(creatableName)) {
                    creatableObjects.cmd(prompt);
                    return;
                }
            }
            Terminal.addText("no creatable object named: " + creatableName);
        }
    }, set("set - sets a given property // sets properties e.g circlesize, fontsize, bgr_color... // Syntax: set <propertyName>(<value>)") {
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
    }, exit("exit - exits the application // // Syntax: <any exit word>") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
            Terminal.addText("Thanks for using JNetVisualFX! - GoodBye");
            Window.exit();
        }
    }, hello("hello - greets the user // // Syntax: <any hello word>") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Hello - good to see you!");
        }
    }, clear("clear - clears the terminal // clears the whole text history for only the terminal // Syntax: clear") {
        @Override
        public void cmd(String prompt) {
            Terminal.clear();
        }
    }, font("font - info about the font // // Syntax: font") {
        @Override
        public void cmd(String prompt) {

            Terminal.addText("Cascadia Code : https://github.com/microsoft/cascadia-code");
        }
    }, github("github - github link to the project // give me a star if you like it ;) // Syntax: github") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("JNetVisualFX's github page: https://github.com/gk646/JNetVisualFX");
        }
    }, version("version - version info // // Syntax: version") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText(Info.VERSION);
        }
    },

    resetall("resetall - resets all properties to default value // // Syntax: resetall") {
        @Override
        public void cmd(String prompt) {
            NetworkVisualizer.maxCircleDiameter = 30;
            SettableProperties.fontsize.cmd("15");
            Terminal.terminalRoot = ">";
            Terminal.addText("reset all value to default state!");
        }
    }, helpall("helpall - lists all commands // lists all commands including their man page // Syntax: helpall") {
        @Override
        public void cmd(String prompt) {
            for (Command command : CommandController.COMMANDS) {
                Log.addLogText(command.getManPage());
            }
        }
    }, help("") {
        @Override
        public void cmd(String prompt) {
            Terminal.addText("man <method or command> for manual the manual page");
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
    }, name("name - name yourself // adds your name to the terminal root; \"reset\" to reset // Syntax: name <name>") {
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
    }, quicksetup("quicksetup - quickly generates a basic Network((4,4,4), sigmoid)// Syntax: quicksetup") {
        @Override
        public void cmd(String prompt) {
            Terminal.parseText("new NetBuilder([3,3,3],sigmoid)");
            Terminal.parseText("new Network");
            Terminal.parseText("list he [23,23,23]");
            Terminal.parseText("list hey [23,23,24]");
            Terminal.parseText("train(hey,he,3)");
        }
    }, listget("") {
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
    }, listall("listall - displays a list of lists created ") {
        @Override
        public void cmd(String prompt) {
            Playground.playgroundLists.forEach((s, matrix) -> Terminal.addText(s + ": " + matrix));
        }
    }, list("list - creates and saves a named list out of the given input; can be floating points ; max depth is 2 ; bracket type = [] // Syntax: list <name> <[[list,of][num,bers]]> ") {
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
    }, train("train - trains your active network; needs data to be in the same shape // Syntax: train(<input-list>,<target-list>,<repetitions>)") {
        final String syntax = "(<input-list>,<target-list>,<repetitions>)";

        @Override
        public void cmd(String prompt) {
            if (Playground.neuralNetwork == null) {
                Terminal.addText("no neural network!");
                return;
            }

            prompt = prompt.replace("train(", "");
            if (prompt.isBlank() || prompt.equals("train")) {
                Terminal.addText("missing list names: " + this + syntax);
                return;
            }
            if (!prompt.contains(")")) {
                Terminal.addText("missing closing bracket: " + this + syntax);
                return;
            }
            if (StringUtil.countChar(prompt, ',') != 2) {
                Terminal.addText("missing commas/arguments: " + this + syntax);
                return;
            }

            String[] arguments = prompt.substring(0, prompt.length() - 1).split(",");
            /*
            if (arguments.length < 3) {
                Terminal.addText("Invalid command, not enough arguments provided: " + this + syntax);
                return;
            }

             */
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

            Playground.neuralNetwork.train(input.getRawData(), input.getRawData(), repetitions);
        }
    }, $("$ - denotes a variable// Syntax: $<variable-Name>") {
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
    }, reset_user_statistics("reset-user-statistics - resets the user statistics completely; only necessary when user-statistics.txt is unreadable ;USE WITH CARE // Syntax: reset-user-statistics") {
        @Override
        public String toString() {
            return "reset-user-statistics";
        }

        @Override
        public void cmd(String prompt) {
            Window.localFileSaver.resetUserStatistic();
        }
    }, reset("reset - reset the playground // reset both the NetBuilder and current Network to null // Syntax: reset") {
        @Override
        public void cmd(String prompt) {
            Playground.reset();
            Terminal.addText("playground was reset");
        }
    },

    getStat("getStat - retrieves a statistic; returns the most updated value // Syntax: getStat <stat-name>") {
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
    };

    private final String manPage;
    Matcher matcher;

    Command(String manPage) {
        this.manPage = manPage;
    }

    public abstract void cmd(String prompt);

    public String getManPage() {
        return manPage;
    }
}