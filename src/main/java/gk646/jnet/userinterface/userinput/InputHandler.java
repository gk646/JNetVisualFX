package gk646.jnet.userinterface.userinput;

import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.input.KeyEvent;

public final class InputHandler {
    private final String ENTER = "\r";
    private final String BACKSPACE = "\b";
    private byte commandHistoryOffset = -1;
    private boolean CTRLpressed;
    private boolean preventInput;

    public InputHandler() {
    }


    public void handleKeyType(KeyEvent event) {
        String activeCharacter = event.getCharacter();
        switch (activeCharacter) {
            case ENTER -> {
                Terminal.parseText(Terminal.currentText.toString());
                Terminal.currentText = new StringBuilder();
                commandHistoryOffset = -1;
                Terminal.cursorOffsetLeft = 0;

                return;
            }
            case BACKSPACE -> {
                int length = Terminal.currentText.length();
                if (length > 0) {
                    if (Terminal.cursorOffsetLeft == 0) {
                        Terminal.currentText.deleteCharAt(length - 1);
                    } else if (Terminal.cursorOffsetLeft != length) {
                        Terminal.currentText.deleteCharAt(length - 1 - Terminal.cursorOffsetLeft);
                    }
                }
                return;
            }
        }
        if (!CTRLpressed) {
            if (Terminal.cursorOffsetLeft == 0) {
                Terminal.currentText.append(event.getCharacter());
            } else {
                Terminal.currentText.insert(Terminal.currentText.length() - Terminal.cursorOffsetLeft, event.getCharacter());
            }
        }
    }

    public void handleSpecialKeyType(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> {
                if (!Terminal.currentText.isEmpty()) {
                    return;
                }
                if (commandHistoryOffset < Terminal.commandHistory.size() - 1) {
                    commandHistoryOffset++;
                }
                Terminal.cursorOffsetLeft = 0;
                Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(commandHistoryOffset));
            }
            case DOWN -> {
                if (!Terminal.currentText.isEmpty()) {
                    return;
                }
                if (commandHistoryOffset > 0) {
                    commandHistoryOffset--;
                } else {
                    Terminal.currentText = new StringBuilder();
                    commandHistoryOffset = -1;
                    return;
                }
                Terminal.cursorOffsetLeft = 0;
                Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(commandHistoryOffset));
            }

            case CONTROL -> CTRLpressed = true;

            case PLUS -> {
                if (CTRLpressed) {
                    Terminal.changeFontSize(1);
                }
            }
            case MINUS -> {
                if (CTRLpressed) {
                    Terminal.changeFontSize(-1);
                }
            }
            case LEFT -> {
                if (Terminal.cursorOffsetLeft < Terminal.currentText.length()) {
                    Terminal.cursorOffsetLeft++;
                }
            }
            case RIGHT -> {
                if (Terminal.cursorOffsetLeft >= 1) {
                    Terminal.cursorOffsetLeft--;
                }
            }
        }
    }

    public void handleSpecialKeyLift(KeyEvent event) {
        switch (event.getCode()) {
            case CONTROL -> CTRLpressed = false;
        }
    }
}
