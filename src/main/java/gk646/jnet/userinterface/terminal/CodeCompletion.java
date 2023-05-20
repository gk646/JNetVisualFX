package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.util.datastructures.Trie;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CodeCompletion {
    private static final Trie trie = new Trie();
    private static final Color backGround = Colors.INTELLIJ_GREY;
    final ArrayList<String> commandList = new ArrayList<>(Arrays.stream(CommandController.COMMANDS).map(Enum::toString).toList());
    private static List<String> currentCompletions;
    private static boolean inSpecialNameSpace;

    CodeCompletion() {
        for (Command command : CommandController.COMMANDS) {
            trie.insert(command.toString());
        }
        for (Method method : Parser.getMethodMap().values()) {
            trie.insert(method.getName());
        }
    }

    public void draw(GraphicsContext gc) {
        List<String> completions = autoComplete();
        currentCompletions = completions;
        int length = completions.size();
        int startY = Terminal.containerHelper.getDrawY() + Terminal.containerHelper.getHeight() - Terminal.lineHeight;
        startY -= length * Terminal.lineHeight;

        int startX = Terminal.containerHelper.getDrawX();
        for (String text : completions) {
            gc.setFill(backGround);
            gc.fillRect(startX, startY - 13, 180, Terminal.lineHeight);
            gc.setFill(Terminal.text);
            gc.fillText(text, startX, startY);
            startY += Terminal.lineHeight;
        }
    }


    public List<String> getCodeCompletions() {
        return trie.autoComplete(Terminal.currentText.toString());
    }

    public List<String> autoComplete() {
        if (Terminal.currentText.isEmpty()) return new ArrayList<>();

        String input = Terminal.currentText.toString();
        if (input.startsWith("set ")) {
            String newInput = input.replace("set ", "");
            inSpecialNameSpace= true;
            return CommandController.settableProperties.stream().filter(word -> word.startsWith(newInput) && !word.equals(newInput)).toList();
        } else if (input.startsWith("new ")) {
            inSpecialNameSpace= true;
            String newInput = input.replace("new ", "");
            return CommandController.creatableObjects.stream().filter(word -> word.startsWith(newInput) && !word.equals(newInput)).toList();
        } else {
            inSpecialNameSpace= false;
            return commandList.stream().filter(word -> word.startsWith(input) && !word.equals(input)).toList();
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
}
