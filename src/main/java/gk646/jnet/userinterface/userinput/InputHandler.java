package gk646.jnet.userinterface.userinput;

import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.input.KeyEvent;

public final class InputHandler {
    public static StringBuilder currentText = new StringBuilder();
    private final String ENTER = "\r";
    private final String BACKSPACE = "\b";

    public InputHandler() {
    }


    public void update() {

    }


    public void handleKeyType(KeyEvent event) {
        String activeCharacter = event.getCharacter();

        switch (activeCharacter) {
            case ENTER -> {
                Terminal.addText(currentText.toString());
                currentText = new StringBuilder();
                return;
            }
            case BACKSPACE -> {
                int length = currentText.length();
                if (length > 0) {
                    currentText.deleteCharAt(length - 1);
                }
                return;
            }
        }
        currentText.append(event.getCharacter());
    }
}
