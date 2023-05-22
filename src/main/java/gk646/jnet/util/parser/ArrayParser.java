package gk646.jnet.util.parser;

import gk646.jnet.userinterface.terminal.Terminal;

import java.util.ArrayDeque;

public class ArrayParser {


    public ArrayParser() {

    }

    public void parseList(String prompt, String listName) {
        var stack = new ArrayDeque<Boolean>();
        var currentWord = new StringBuilder();

        char[] promptArr = prompt.toCharArray();
        int depth = 0;
        for (char currentChar : promptArr) {     // [1,1]
            switch (currentChar) {
                case '[', '(', '{' -> depth++;
            }
        }
        if (depth > 3) {
            Terminal.addText("array depth exceeds 3:" + prompt);
            return;
        }


        for (char currentChar : promptArr) {     // [1,1]
            switch (currentChar) {
                case '[', '(', '{' -> stack.add(true);
                case ']', ')', '}' -> {
                    if (stack.isEmpty()) {
                        Terminal.addText("invalid array declaration:" + prompt);
                        return;
                    }
                    stack.pop();
                }
            }
            currentWord.append(currentWord);
        }
    }
}
