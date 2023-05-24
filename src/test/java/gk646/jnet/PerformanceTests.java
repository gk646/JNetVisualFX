package gk646.jnet;

import java.util.Collections;

public class PerformanceTests {
    public static int countNewlines(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') count++;
        }
        return count;
    }
    public static int countNewline(String s, char in) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '\n') count++;
        }
        return count;
    }
    public static int countCharStreams(String s, char in) {
        return (int) s.chars().filter(c -> c == in).count();
    }
    public static int countNewlinesFast(String s, char in) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == in) count++;
        }
        return count;
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


    public static void main(String[] args) {
        String s = String.join("", Collections.nCopies(100, "a"));
        var str = new StringBuilder(s);
        str.insert(30,"\n");
        str.insert(60,"\n");
        s = str.toString();
        char in = '\n';

        long startTime, elapsedTime;

        // Test countNewline


        // Test countCharStreams
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            countCharStreams(s, in);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed Time for countCharStreams: " + elapsedTime / 1000000.0 + " ms");

        // Test countNewlinesFast
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            countNewlinesFast(s, in);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed Time for countNewlinesFast: " + elapsedTime / 1000000.0 + " ms");


        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            countNewline(s, in);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed Time for countNewline: " + elapsedTime / 1000000.0 + " ms");
    }
}
