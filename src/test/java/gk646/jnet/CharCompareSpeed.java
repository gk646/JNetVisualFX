package gk646.jnet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CharCompareSpeed {
    static final char[] warning = new char[]{'[', 'W'};
    static final char[] severe = new char[]{'[', 'S'};

    private static void compare(List<String> test) {
        char[] prefix = new char[2];
        for (String s : test)
            s.getChars(0, 2, prefix, 0);
        if (Arrays.equals(prefix, warning)) {

        } else if (Arrays.equals(prefix, severe)) {

        } else {

        }
    }
    private static void compareStart(List<String> test) {
        for (String s : test)

        if (s.startsWith("[W")) {

        } else if (s.startsWith("[S")) {

        } else {

        }
    }
    public static void main(String[] args) {
        var list = new java.util.ArrayList<>(Collections.nCopies(100, "[W"));
        list.set(10,"[S");
        list.set(50,"[S");
        list.set(70,"[S");
        list.set(30,"[S");
        list.set(90,"[S");

        long startTime, elapsedTime;

        // Test countNewline


        // Test countCharStreams
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
           compareStart(list);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed Time for compareStart: " + elapsedTime / 1000000.0 + " ms");

        // Test countNewlinesFast
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            compare(list);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("Elapsed Time for compare: " + elapsedTime / 1000000.0 + " ms");
    }
}
