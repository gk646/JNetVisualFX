package gk646.jnet.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static int countChar(String s, char in) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == in) count++;
        }
        return count;
    }

    public static int countNewlines(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '\n') count++;
        }
        return count;
    }

    public static String insertNewLines(String input, int maxCharsPerLine) {
        if (input.length() < maxCharsPerLine) return input;
        StringBuilder s = new StringBuilder(input);
        for (int j = 1; j < 5; j++) {
            if (s.length() < maxCharsPerLine * j + j) {
                break;
            } else if (s.length() >= maxCharsPerLine * j + j) {
                s.insert(maxCharsPerLine * j + j, "\n");
            }
        }
        return s.toString();
    }

    public static boolean containsAnyNumbers(String s) {
        for (char c : s.toCharArray()) {
            switch (c) {
                case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    return true;
                }
            }
        }
        return false;
    }
}
