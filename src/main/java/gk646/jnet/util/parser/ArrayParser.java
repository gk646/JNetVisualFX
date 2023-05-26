package gk646.jnet.util.parser;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.util.datastructures.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ArrayParser {
    private static final String invalidDeclaration = "invalid array declaration:";

    public ArrayParser() {
    }

    public void parseList(String prompt, String listName) {
        if (!prompt.contains("[") || !prompt.contains("]")) {
            {
                Terminal.addText(invalidDeclaration + prompt);
                return;
            }
        }
        if (prompt.equals("[]") || prompt.equals("[[]]")) {
            Playground.playgroundLists.put(listName, new Matrix(0, 0));
            Terminal.addText("empty list added: " + listName);
            return;
        }
        if (prompt.length() < 2) {
            Terminal.addText(invalidDeclaration + prompt);
            return;
        }
        boolean twoDimensional = prompt.startsWith("[[") && prompt.endsWith("]]");
        if (!twoDimensional && (prompt.startsWith("[[") || prompt.endsWith("]]"))) {
            Terminal.addText(invalidDeclaration + prompt);
            return;
        }

        int depth = 0;
        for (char currentChar : prompt.toCharArray()) {
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

        UserStatistics.updateStat(UserStatistics.Stat.arraysCreated,1);

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

        List<double[]> numberList = new ArrayList<>(arrays.length);
        for (String numbers : arrays) {
            numberList.add(parseArrayFromString(numbers));
        }

        UserStatistics.updateStat(UserStatistics.Stat.arraysCreated,1);

        if (Playground.playgroundLists.put(listName, new Matrix(numberList.toArray(new double[arrays.length][]))) != null) {
            Terminal.addText("updated list: " + listName);
        } else {
            Terminal.addText("added new list: " + listName);
        }
    }


    public double[] parseArrayFromString(String prompt) {
        String[] numbers = prompt.split(",");
        double[] arr = new double[numbers.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Double.parseDouble(numbers[i]);
        }
        return arr;
    }

    public int[] parseIntArrayFromString(String prompt) {
        String[] numbers = prompt.split(",");
        int[] arr = new int[numbers.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Integer.parseInt(numbers[i]);
        }
        return arr;
    }
}
