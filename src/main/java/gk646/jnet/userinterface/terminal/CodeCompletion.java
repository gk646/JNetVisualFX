package gk646.jnet.userinterface.terminal;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.terminal.commands.CreatableObjects;
import gk646.jnet.userinterface.terminal.commands.SettableProperties;
import gk646.jnet.util.datastructures.Trie;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CodeCompletion {
    private final Trie allCommandsTrie = Trie.root();
    private final Trie setTrie = Trie.root();
    private final Trie manTrie = Trie.root();
    private final Trie statsTrie = Trie.root();
    private final Trie creatableTrie = Trie.root();
    private static final Color backGround = Colors.INTELLIJ_GREY;
    private static List<String> currentCompletions = new ArrayList<>();
    private static boolean inSpecialNameSpace;
    private String previousPrompt = "";

    CodeCompletion() {
        List<String> settableProperties = Arrays.stream(SettableProperties.values()).map(Enum::toString).toList();
        List<String> creatableObjects = Arrays.stream(CreatableObjects.values()).map(Enum::toString).toList();
        ArrayList<String> commandList = new ArrayList<>(Arrays.stream(CommandController.COMMANDS).map(Enum::toString).toList());
        List<String> userStatistics = Arrays.stream(UserStatistics.Stat.values()).map(Enum::toString).toList();
        commandList.addAll(Parser.getMethodMap().values().stream().map(Method::getName).toList());

        allCommandsTrie.insertList(commandList);
        allCommandsTrie.insertList(Parser.getMethodMap().values().stream().map(Method::getName).toList());

        setTrie.insertList(settableProperties);

        manTrie.insertList(commandList);
        manTrie.insertList(creatableObjects);
        manTrie.insertList(settableProperties);

        statsTrie.insertList(userStatistics);

        creatableTrie.insertList(creatableObjects);
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
            gc.setFill(Terminal.TerminalInfo.textColor);
            gc.fillText(text, startX, startY);
            startY += Terminal.TerminalInfo.lineHeight;
        }
    }


    public List<String> autoComplete() {
        if (Terminal.currentText.isEmpty()) return Collections.emptyList();

        String input = Terminal.currentText.toString();
        if (input.startsWith("set ")) {
            inSpecialNameSpace = true;
            return setTrie.autoCompleteWithAllOnEmpty(input.replace("set ", ""));
        } else if (input.startsWith("new ")) {
            inSpecialNameSpace = true;
            return creatableTrie.autoCompleteWithAllOnEmpty(input.replace("new ", ""));
        } else if (input.startsWith("getStat ")) {
            inSpecialNameSpace = true;
            return statsTrie.autoCompleteWithAllOnEmpty(input.replace("getStat ", ""));
        } else if (input.startsWith("man ")) {
            inSpecialNameSpace = true;
            return manTrie.autoCompleteWithAllOnEmpty(input.replace("man ", ""));
        } else {
            inSpecialNameSpace = false;
            return allCommandsTrie.autoComplete(input);
        }
    }

    private boolean isNewPrompt() {
        if (previousPrompt.equals(Terminal.currentText.toString())) {
            return false;
        }
        this.previousPrompt = Terminal.currentText.toString();
        return true;
    }
}
