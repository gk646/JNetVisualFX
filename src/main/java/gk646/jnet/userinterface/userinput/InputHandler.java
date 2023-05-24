package gk646.jnet.userinterface.userinput;

import gk646.jnet.userinterface.terminal.CodeCompletion;
import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

public final class InputHandler {
    private static final String ENTER = "\r";
    private static final String BACKSPACE = "\b";
    private static final String TAB = "\t";
    private static final String ESCAPE = "\u001B";
    private static final String DEL = "\u007F";
    public static int commandHistoryOffset = -1;
    private static boolean controlPressed;

    public InputHandler() {
    }

    public void handleKeyType(KeyEvent event) {
        //System.out.println(Arrays.toString(event.getCharacter().getBytes(StandardCharsets.UTF_8)));
        String activeCharacter = event.getCharacter();
        switch (activeCharacter) {
            case TAB, ESCAPE, "\u001D", "\u0016" -> {
                return;
            }
            case ENTER -> {
                commandHistoryOffset = -1;
                Terminal.TerminalInfo.cursorOffsetLeft = 0;
                String prompt = Terminal.currentText.toString();
                Terminal.currentText.setLength(0);
                Terminal.parseText(prompt);
                return;
            }
            case BACKSPACE -> {
                int length = Terminal.currentText.length();
                if (length > 0 && Terminal.TerminalInfo.cursorOffsetLeft < length) {
                    Terminal.currentText.deleteCharAt(length - 1 - Terminal.TerminalInfo.cursorOffsetLeft);
                }
                return;
            }
            case DEL -> {
                if (Terminal.TerminalInfo.cursorOffsetLeft > 0 && Terminal.TerminalInfo.cursorOffsetLeft <= Terminal.currentText.length()) {
                    Terminal.currentText.deleteCharAt(Terminal.currentText.length() - Terminal.TerminalInfo.cursorOffsetLeft);
                }
                return;
            }
        }

        if (Terminal.TerminalInfo.cursorOffsetLeft > 0 && Terminal.TerminalInfo.cursorOffsetLeft <= Terminal.currentText.length()) {
            Terminal.currentText.insert(Terminal.currentText.length() - Terminal.TerminalInfo.cursorOffsetLeft, event.getCharacter());
        } else {
            Terminal.currentText.append(event.getCharacter());
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
                if (controlPressed) Terminal.TerminalInfo.changeFontSize(1);
            }
            case MINUS -> {
                if (controlPressed) Terminal.TerminalInfo.changeFontSize(-1);
            }
            case LEFT -> {
                if (Terminal.TerminalInfo.cursorOffsetLeft < Terminal.currentText.length()) Terminal.TerminalInfo.cursorOffsetLeft++;
            }
            case RIGHT -> {
                if (Terminal.TerminalInfo.cursorOffsetLeft >= 1) Terminal.TerminalInfo.cursorOffsetLeft--;
            }
            case TAB -> {
                if (CodeCompletion.getCurrentCompletions().size() == 1) {
                    Terminal.TerminalInfo.cursorOffsetLeft = 0;
                    if (CodeCompletion.isInSpecialNameSpace()) {
                        Terminal.currentText = new StringBuilder(Terminal.currentText.substring(0, Terminal.currentText.indexOf(" ") + 1));
                        Terminal.currentText.append(CodeCompletion.getCurrentCompletions().get(0));
                        return;
                    }
                    Terminal.currentText = new StringBuilder(CodeCompletion.getCurrentCompletions().get(0));
                }
            }
            case V -> {
                if (controlPressed) {
                    String content = Clipboard.getSystemClipboard().getString();
                    if (content != null) {
                        Terminal.currentText.append(content);
                    }
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
