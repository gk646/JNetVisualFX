package gk646.jnet.util.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Trie {
    private static final int ALPHABET_SIZE = 55;
    private final Trie[] children = new Trie[ALPHABET_SIZE];
    private String text;

    private final char character;

    private Trie(char c) {
        this.character = c;
    }

    public boolean isWord() {
        return text != null;
    }

    public static Trie root() {
        return new Trie('L');
    }

    public void insertList(List<String> list) {
        for (String val : list) {
            this.insert(val, val);
        }
    }

    public void insert(String word, String fullWord) {
        if (word == null || word.isEmpty()) {
            return;
        }

        char firstChar = word.charAt(0);
        Trie child = children[charToIndex(firstChar)];
        if (child == null) {
            child = new Trie(firstChar);
            children[charToIndex(firstChar)] = child;
        }

        if (word.length() > 1) {
            child.insert(word.substring(1), fullWord);
        } else {
            child.text = fullWord;
        }
    }

    private int charToIndex(char c) {
        if (c >= 97) return c - 97;
        if (c >= 65 && c <= 91) return c - 65 + 26;
        if (c == '_') return 52;
        if (c == '$') return 53;
        if (c == '-') return 54;
        return -1;
    }



    public List<String> autoComplete(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Collections.emptyList();
        }

        Trie node = this;
        for (char c : prefix.toCharArray()) {
            if (node.children[charToIndex(c)] == null) {
                return Collections.emptyList();
            } else {
                node = node.children[charToIndex(c)];
            }
        }

        if (node.isWord() && !node.text.equals(prefix)) {
            return List.of(node.text);
        }
        var results = new ArrayList<String>(15);
        findAllChildWords(node, results, new StringBuilder().append(prefix));
        return results;
    }

    private void findAllChildWords(Trie node, List<String> results, StringBuilder word) {
        for (Trie child : node.children) {
            if (child == null) continue;
            word.append(child.character);
            if (child.isWord()) {
                results.add(word.toString());
            }
            findAllChildWords(child, results, word);
            word.deleteCharAt(word.length() - 1);
        }
    }
}
