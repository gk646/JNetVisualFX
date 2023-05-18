package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.Terminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SetCommand {

    circlesize {
        private static final Pattern pattern = Pattern.compile("\\((.*?)\\)");

        @Override
        public String cmd(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                int newCircleSize = Integer.parseInt(matcher.group(1));
                if (newCircleSize > 0 && newCircleSize < 100) {
                    NetworkVisualizer.MAX_CIRCLE_DIAMETER = newCircleSize;
                    return "set new circleSize: " + newCircleSize;
                }
                return "invalid circlesize: " + newCircleSize;
            }
            return "couldnt parse circlesize: " + prompt;
        }
    },
    fontsize {
        private static final Pattern pattern = Pattern.compile("\\((.*?)\\)");
        @Override
        public String cmd(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                int newFontSize = Integer.parseInt(matcher.group(1));
                if (newFontSize > 0 && newFontSize < 50) {
                    Terminal.changeFontSize(newFontSize);
                    return "set new fontsize: " + newFontSize;
                }
                return "invalid fontsize: " + newFontSize;
            }
            return "couldnt parse fontsize: " + prompt;
        }
    };
    public Matcher matcher;

    SetCommand() {
    }

    public abstract String cmd(String prompt);
}
