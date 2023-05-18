package gk646.jnet.util.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    private HashMap<Character, Trie> children = new HashMap<>();
    private String text = "";


    public boolean isWord() {
        return !text.isEmpty();
    }


    public Trie getChild(Character c) {
        return children.get(c);
    }


    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        Character firstChar = word.charAt(0);

        Trie child = getChild(firstChar);
        if (child == null) {
            child = new Trie();
            children.put(firstChar, child);
        }

        if (word.length() > 1) {
            child.insert(word.substring(1));
        } else {
            child.text = word;
        }
    }

    public ArrayList<String> autoComplete(String prefix) {
        ArrayList<String> results = new ArrayList<>();

        if (prefix == null || prefix.isEmpty()) {
            return results;
        }

        Trie node = this;
        for (char c : prefix.toCharArray()) {
            if (node.children.get(c) == null) {
                return results;
            } else {
                node = node.children.get(c);
            }
        }

        if (node.isWord()) {
            results.add(node.text);
        }

        findAllChildWords(node, results, new StringBuilder().append(prefix.charAt(0)));

        return results;
    }

    private void findAllChildWords(Trie node, List<String> results, StringBuilder word) {
        for (char c : node.children.keySet()) {
            StringBuilder newWord = new StringBuilder(word.toString());
            newWord.append(c);
            Trie childNode = node.getChild(c);
            if (childNode.isWord()) {
                results.add(newWord.toString());
            }
            findAllChildWords(childNode, results, newWord);
        }
    }
}
