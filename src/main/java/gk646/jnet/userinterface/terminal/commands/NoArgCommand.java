package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.Info;
import gk646.jnet.userinterface.Window;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;

public enum NoArgCommand {

    exit {

        @Override
        public boolean cmd(String prompt) {
            if (exitStringList.contains(prompt)) {
                Terminal.clear();
                Terminal.addText("Thanks for using JNetVisualFX! - GoodBye");
                Window.exit();
                return true;
            }
            return false;
        }
    },
    HELLO {

        @Override
        public boolean cmd(String prompt) {
            if (helloList.contains(prompt)) {
                Terminal.addText("Hello - good to see you!");
                return true;
            }
            return false;
        }
    },
    clear {
        @Override
        public boolean cmd(String prompt) {
            if (prompt.equals(toString())) {
                Terminal.clear();
                return true;
            }
            return false;
        }
    },
    reset {
        @Override
        public boolean cmd(String prompt) {
            if (prompt.equals(toString())) {
                Playground.reset();
                Terminal.addText("playground was reset");
                return true;
            }
            return false;
        }
    },
    font {
        @Override
        public boolean cmd(String prompt) {
            if (prompt.equals(toString())) {
                Terminal.addText("Cascadia Code : https://github.com/microsoft/cascadia-code");
                return true;
            }
            return false;
        }
    },
    github {
        @Override
        public boolean cmd(String prompt) {
            if (prompt.equals(toString())) {
                Terminal.addText("JNetVisualFX's github page: https://github.com/gk646/JNetVisualFX");
                return true;
            }
            return false;
        }
    },
    version {
        @Override
        public boolean cmd(String prompt) {
            if (prompt.equals(toString())) {
                Terminal.addText(Info.VERSION);
                return true;
            }
            return false;
        }
    };
    public static final ArrayList<String> helloList = new ArrayList<>(List.of("hello", "hey"));
    public static final ArrayList<String> exitStringList = new ArrayList<>(List.of("exit", "logout", "shutdown", "goodbye", "bye", "byebye", "cya"));

    public abstract boolean cmd(String prompt);
}
