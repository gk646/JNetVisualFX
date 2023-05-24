package gk646.jnet.userinterface.terminal.commands;

import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.terminal.Terminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SettableProperties {

    circlesize("circlesize - settable property with: set<propertyName> // maximum size of the neurons: 0 - 100, Default: 20") {
        @Override
        public String cmd(String prompt) {
            matcher = pattern.matcher(prompt);
            if (matcher.find()) {
                int newCircleSize = Integer.parseInt(matcher.group(1));
                if (newCircleSize > 0 && newCircleSize < 100) {
                    NetworkVisualizer.maxCircleDiameter = newCircleSize;
                    return "set new circleSize: " + newCircleSize;
                }
                return "invalid circlesize: " + newCircleSize;
            }
            return "couldnt parse circlesize (<circlesize): " + prompt;
        }
    },
    fontsize("fontsize - settable property with: set<propertyName> // fontsize of the terminal font: 0 - 50, Default: 15") {
        @Override
        public String cmd(String prompt) {
            int parsedNum;
            try {
                parsedNum = Integer.parseInt(prompt);
            } catch (NumberFormatException ignored) {
                return "couldnt parse " + fontsize + " <" + fontsize + ">: " + prompt;
            }

            if (parsedNum > 0 && parsedNum < 50) {
                int fontSizeDelta = parsedNum - Terminal.getFontSize();
                Terminal.changeFontSize(fontSizeDelta);
                return "set new fontsize: " + parsedNum;
            }
            return "invalid fontsize: " + parsedNum;
        }
    };
    private static final Pattern pattern = Pattern.compile("\\((.*?)\\)");
    private final String manPage;
    Matcher matcher;

    SettableProperties(String manPage) {
        this.manPage = manPage;
    }

    public abstract String cmd(String prompt);

    public String getManPage() {
        return manPage;
    }
}
