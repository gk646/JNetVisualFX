package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.terminal.commands.ArgCommand;
import gk646.jnet.userinterface.terminal.commands.NoArgCommand;
import gk646.jnet.util.datastructures.Trie;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class CodeCompletion {
    public static Trie trie = new Trie();
    public static Color backGround = Colors.INTELLIJ_GREY;
    final ArrayList<String> wordSet = new ArrayList<>(50);
    public static List<String> currentCompletions;

    CodeCompletion() {
        for (NoArgCommand noArgCommand : NoArgCommand.values()) {
            trie.insert(noArgCommand.name());
            wordSet.add(noArgCommand.name());
        }
        for (String string : NoArgCommand.exitStringList) {
            trie.insert(string);
            wordSet.add(string);
        }
        for (String string : NoArgCommand.helloList) {
            trie.insert(string);
            wordSet.add(string);
        }
        for (ArgCommand argCommand : ArgCommand.values()) {
            trie.insert(argCommand.getKeyWord());
            wordSet.add(argCommand.getKeyWord());
        }
        for (Method method : Parser.getMethodMap().values()) {
            trie.insert(method.getName());
            wordSet.add(method.getName());
        }
    }

    public void draw(GraphicsContext gc) {
        List<String> completions = autoComplete();
        currentCompletions = completions;
        int length = completions.size();
        int startY = Terminal.containerHelper.getDrawY() + Terminal.containerHelper.getHeight() - Terminal.LINE_HEIGHT;
        startY -= length * Terminal.LINE_HEIGHT;

        int startX = Terminal.containerHelper.getDrawX();
        for (String text : completions) {
            gc.setFill(backGround);
            gc.fillRect(startX, startY - 13, 100, Terminal.LINE_HEIGHT);
            gc.setFill(Terminal.text);
            gc.fillText(text, startX, startY);
            startY += Terminal.LINE_HEIGHT;
        }
    }


    public ArrayList<String> getCodeCompletions() {
        return trie.autoComplete(Terminal.currentText.toString());
    }

    public List<String> autoComplete() {
        if (Terminal.currentText.isEmpty()) return new ArrayList<>();
        String input = Terminal.currentText.toString();
        return wordSet.stream()
                .filter(word -> word.startsWith(input) && !word.equals(input))
                .toList();
    }


    public static boolean blockCommandHistory() {
        if (!Terminal.currentText.isEmpty()) {
            return !CodeCompletion.currentCompletions.isEmpty();
        }
        return false;
    }
}
