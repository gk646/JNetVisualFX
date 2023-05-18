package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.terminal.commands.ArgCommand;
import gk646.jnet.userinterface.terminal.commands.NoArgCommand;
import gk646.jnet.util.datastructures.Trie;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class CodeCompletion {
    public static Trie trie = new Trie();

    CodeCompletion() {
        for (NoArgCommand noArgCommand : NoArgCommand.values()) {
            trie.insert(noArgCommand.name());
        }
        for (String string : NoArgCommand.exitStringList) {
            trie.insert(string);
        }
        for (String string : NoArgCommand.helloList) {
            trie.insert(string);
        }

        for (ArgCommand argCommand : ArgCommand.values()) {
            trie.insert(argCommand.getKeyWord());
        }
    }

    public void draw(GraphicsContext gc) {
        ArrayList<String> completions = getCodeCompletions();
        int length = completions.size();
        for (int i = 0; i < length; i++) {

        }
    }


    public ArrayList<String> getCodeCompletions() {
        return trie.autoComplete(Terminal.currentText.toString());
    }
}
