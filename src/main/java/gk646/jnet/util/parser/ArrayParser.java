package gk646.jnet.util.parser;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.util.datastructures.Matrix;

import java.util.ArrayList;
import java.util.List;

public class ArrayParser {
    private static final String invalidDeclaration = "invalid array declaration:";

    public ArrayParser() {
    }

    public void parseList(String prompt, String listName) {
        int depth = 0;
        boolean twoDimensional = prompt.charAt(0) == '[' && prompt.charAt(1) == '[';  // still couldnt go wrong [23,[]
        if(twoDimensional && !(prompt.charAt(prompt.length()-1) == ']' && prompt.charAt(prompt.length()-2) == ']')){
            Terminal.addText(invalidDeclaration + prompt);
            return;
        }
        for (char currentChar : prompt.toCharArray()) {// [1,1]
            switch (currentChar) {
                case '[' -> depth++;
                case ']' -> depth--;
            }
            if (depth > 2) {
                Terminal.addText("array depth exceeds 2:" + prompt);
                return;
            } else if (depth == 2 && !twoDimensional) {
                Terminal.addText(invalidDeclaration + prompt);
                return;
            }
        }
        if (prompt.length() == 2) {
            Playground.playgroundLists.put(listName, new Matrix(0, 0));
            Terminal.addText("empty list added: " + listName);
            return;
        }
        if (depth != 0) {
            Terminal.addText(invalidDeclaration + prompt);
            return;
        }

        if (twoDimensional) {
            parseTwoDimensionalArray(prompt, listName);
        } else {
            parseOneDimensionalArray(prompt, listName);
        }
    }

    private void parseOneDimensionalArray(String prompt, String listName) { // [2,3,4,5]
        double[] arr = parseArrayFromString(prompt.substring(1, prompt.length() - 1));

        if (Playground.playgroundLists.put(listName, new Matrix(arr)) != null) {
            Terminal.addText("updated list: " + listName);
        } else {
            Terminal.addText("added new list: " + listName);
        }
    }


    private void parseTwoDimensionalArray(String prompt, String listName) { //list input [[2,3],[2,3],[2,3]]
        prompt = prompt.replace("],[", " ");
        prompt = prompt.replace("][", " ");
        String[] arrays = prompt.substring(2, prompt.length() - 2).split(" ");

        List<double[]> numberLists = new ArrayList<>(arrays.length);
        for (String numbers : arrays) {
            numberLists.add(parseArrayFromString(numbers));
        }

        if (Playground.playgroundLists.put(listName, new Matrix(numberLists.toArray(new double[arrays.length][]))) != null) {
            Terminal.addText("updated list: " + listName);
        } else {
            Terminal.addText("added new list: " + listName);
        }
    }


    private double[] parseArrayFromString(String prompt) {
        String[] numbers = prompt.split(",");
        double[] arr = new double[numbers.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Double.parseDouble(numbers[i]);
        }
        return arr;
    }
}
