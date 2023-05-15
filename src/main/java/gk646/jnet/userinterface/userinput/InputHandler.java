package gk646.jnet.userinterface.userinput;

import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.input.KeyEvent;

public final class InputHandler {
    private final String ENTER = "\r";
    private final String BACKSPACE = "\b";
    private byte commandHistoryOffset = -1;

    public InputHandler() {
    }


    public void update() {

    }


    public void handleKeyType(KeyEvent event) {
        String activeCharacter = event.getCharacter();
        switch (activeCharacter) {
            case ENTER -> {
                Terminal.changeFontSize(-1);
                Terminal.parseText(Terminal.currentText.toString());
                Terminal.currentText = new StringBuilder();
                commandHistoryOffset = -1;
                return;
            }
            case BACKSPACE -> {
                int length = Terminal.currentText.length();
                if (length > 0) {
                    Terminal.currentText.deleteCharAt(length - 1);
                }
                return;
            }
        }
        Terminal.currentText.append(event.getCharacter());
    }

    public void handleSpecialKeyType(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> {
                if (commandHistoryOffset < Terminal.commandHistory.size()-1) {
                    commandHistoryOffset++;
                }
                Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(commandHistoryOffset));

            }
            case DOWN -> {
                if (commandHistoryOffset >= 1) {
                    commandHistoryOffset--;
                }
                Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(commandHistoryOffset));
            }
        }
    }
}
