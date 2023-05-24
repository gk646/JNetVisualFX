package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.util.datastructures.Trie;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CodeCompletion {
    private static final Trie trie = new Trie();
    private static final Color backGround = Colors.INTELLIJ_GREY;
    private static List<String> currentCompletions = new ArrayList<>();
    private static boolean inSpecialNameSpace;
    final ArrayList<String> commandList = new ArrayList<>(Arrays.stream(CommandController.COMMANDS).map(Enum::toString).toList());
    private String previousPrompt = "";

    CodeCompletion() {
        for (Command command : CommandController.COMMANDS) {
            trie.insert(command.toString());
        }
        for (Method method : Parser.getMethodMap().values()) {
            trie.insert(method.getName());
        }
    }

    public static boolean blockCommandHistory() {
        if (!Terminal.currentText.isEmpty()) {
            return !CodeCompletion.currentCompletions.isEmpty();
        }
        return false;
    }

    public static List<String> getCurrentCompletions() {
        return currentCompletions;
    }

    public static boolean isInSpecialNameSpace() {
        return inSpecialNameSpace;
    }

    public void draw(GraphicsContext gc) {
        if (isNewPrompt()) {
            currentCompletions = autoComplete();
        }
        int length = currentCompletions.size();
        int startY = Terminal.containerHelper.getDrawY() + Terminal.containerHelper.getHeight() - Terminal.TerminalInfo.lineHeight;
        startY -= length * Terminal.TerminalInfo.lineHeight;

        int startX = Terminal.containerHelper.getDrawX();
        for (String text : currentCompletions) {
            gc.setFill(backGround);
            gc.fillRect(startX, startY - 13, 180, Terminal.TerminalInfo.lineHeight);
            gc.setFill(Terminal.TerminalInfo.text);
            gc.fillText(text, startX, startY);
            startY += Terminal.TerminalInfo.lineHeight;
        }
    }

    public List<String> getCodeCompletions() {
        return trie.autoComplete(Terminal.currentText.toString());
    }

    public List<String> autoComplete() {
        if (Terminal.currentText.isEmpty()) return Collections.emptyList();

        String input = Terminal.currentText.toString();
        if (input.startsWith("set ")) {
            String newInput = input.replace("set ", "");
            inSpecialNameSpace = true;
            return CommandController.settableProperties.stream().filter(word -> word.startsWith(newInput) && !word.equals(newInput)).toList();
        } else if (input.startsWith("new ")) {
            inSpecialNameSpace = true;
            String newInput = input.replace("new ", "");
            return CommandController.creatableObjects.stream().filter(word -> word.startsWith(newInput) && !word.equals(newInput)).toList();
        } else if (input.startsWith("getStat ")) {
            inSpecialNameSpace = true;
            String newInput = input.replace("getStat ", "");
            return CommandController.userStatistics.stream().filter(word -> word.startsWith(newInput) && !word.equals(newInput)).toList();
        } else {
            inSpecialNameSpace = false;
            return commandList.stream().filter(word -> word.startsWith(input) && !word.equals(input)).toList();
        }
    }

    private boolean isNewPrompt() {
        if (previousPrompt.contentEquals(Terminal.currentText)) {
            return false;
        }
        this.previousPrompt = Terminal.currentText.toString();
        return true;
    }
}
