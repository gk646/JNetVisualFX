package gk646.jnet.util;

public class StringUtil {


    public static int countChar(String s, char in) {
        int res = 0;
        for (char c : s.toCharArray()) {
            if (in == c) res++;
        }
        return res;
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
