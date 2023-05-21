package gk646.jnet.userinterface.userinput;

import gk646.jnet.userinterface.terminal.CodeCompletion;
import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Objects;

public final class InputHandler {
    private static final String ENTER = "\r";
    private static final String BACKSPACE = "\b";
    private static final String TAB = "\t";
    private static final String ESCAPE = "\u001B";
    public static int commandHistoryOffset = -1;
    private static boolean controlPressed;
    public InputHandler() {
    }

    public void handleKeyType(KeyEvent event) {
        //System.out.println(Arrays.toString(event.getCharacter().getBytes(StandardCharsets.UTF_8)));
        String activeCharacter = event.getCharacter();
        switch (activeCharacter) {
            case TAB, ESCAPE,"\u001D"  -> {
                return;
            }
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
        if (Terminal.cursorOffsetLeft == 0) {
            Terminal.currentText.append(event.getCharacter());
        } else {
            Terminal.currentText.insert(Terminal.currentText.length() - Terminal.cursorOffsetLeft, event.getCharacter());
        }
    }

    public void handleSpecialKeyType(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> {
                if (CodeCompletion.blockCommandHistory()) return;

                if (commandHistoryOffset < Terminal.commandHistory.size() - 1) {
                    commandHistoryOffset++;
                }
                Terminal.scrollCommandHistory();
            }
            case DOWN -> {
                if (CodeCompletion.blockCommandHistory()) return;

                if (commandHistoryOffset > 0) {
                    commandHistoryOffset--;
                } else {
                    Terminal.currentText = new StringBuilder();
                    commandHistoryOffset = -1;
                    return;
                }
                Terminal.scrollCommandHistory();
            }

            case CONTROL -> controlPressed = true;

            case PLUS -> {
                if (controlPressed) {
                    Terminal.changeFontSize(1);
                }
            }
            case MINUS -> {
                if (controlPressed) {
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
            case TAB -> {
                if (CodeCompletion.getCurrentCompletions().size() == 1) {
                    Terminal.cursorOffsetLeft = 0;
                    if (CodeCompletion.isInSpecialNameSpace()) {
                        Terminal.currentText = new StringBuilder(Terminal.currentText.substring(0, Terminal.currentText.indexOf(" ") + 1));
                        Terminal.currentText.append(CodeCompletion.getCurrentCompletions().get(0));
                        return;
                    }
                    Terminal.currentText = new StringBuilder(CodeCompletion.getCurrentCompletions().get(0));
                }
            }
        }
    }


    public void handleSpecialKeyLift(KeyEvent event) {
        if (Objects.requireNonNull(event.getCode()) == KeyCode.CONTROL) {
            controlPressed = false;
        }
    }
}
