package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.userinterface.terminal.Parser;
import gk646.jnet.util.Manual;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
    MAN(Pattern.compile("print\\((.*?)\\)"), "man", "man - manual // returns the manual page for that command or method //Syntax - man <commandName>") {
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
            return "no command or method named: " + prompt;
        }
    },
    HELP(Pattern.compile("help"), "help", "help - how to use the termianl // returns a help page that allows show how to get information about other components // Syntax - help") {
        @Override
        public String cmd(String prompt) {
            return "man <method or command> for manual the manual page";
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
}
