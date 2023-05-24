package gk646.jnet.util.parser;

import gk646.jnet.userinterface.terminal.Terminal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public final class ArithmeticParser {


     private static final char[] supportedOperators = "+-/*".toCharArray();
    final Lexer lexer;

    public ArithmeticParser() {
        this.lexer = new Lexer();
    }

    private boolean containsExpression(String prompt) {
        for (char c : supportedOperators) {
            for (char pc : prompt.toCharArray())
                if (c == pc) {
                    return true;
                }
        }
        return false;
    }

    public boolean parse(String prompt) {
        if (!containsExpression(prompt)) return false;

        prompt = prompt.replace("print(", "");
        prompt = prompt.replace(")", "");

        var list = lexer.lex(prompt);
        Number parsedNum = evaluateExpression(list);

        parsedNum = formatNumber(parsedNum);

        if (Double.isNaN(parsedNum.doubleValue())) {
            Terminal.addText("invalid arithmetic expr: " + prompt);
        } else {
            Terminal.addText(String.valueOf(parsedNum));
        }

        return true;
    }

    private double evaluateExpression(List<MathToken> tokenList) {
        if (!validTokenList(tokenList)) return Double.NaN;

        var numbers = new ArrayDeque<Double>();
        var operators = new ArrayDeque<MathToken>();


        for (MathToken token : tokenList) {
            if (token.getToken() == ArithmeticToken.NUMBER) {
                numbers.push(token.getVal());
            } else {
                while (!operators.isEmpty() && precedence(token.getToken()) <= precedence(operators.peek().getToken())) {
                    performOperation(numbers, operators);
                }
                operators.push(token);
            }
        }
        while (!operators.isEmpty()) {
            performOperation(numbers, operators);
        }

        if (numbers.size() != 1) {
            return Double.NaN;
        }

        return numbers.pop();
    }

    private int precedence(ArithmeticToken type) {
        return switch (type) {
            case ADD, SUBTRACT -> 1;
            case MULTIPLY, DIVIDE -> 2;
            case NUMBER, MINUS -> 0;
        };
    }

    private void performOperation(Deque<Double> numbers, Deque<MathToken> operators) {
        ArithmeticToken operator = operators.pop().getToken();

        double right = numbers.pop();
        double left = numbers.pop();

        switch (operator) {
            case ADD -> numbers.push(left + right);
            case SUBTRACT -> numbers.push(left - right);
            case MULTIPLY -> numbers.push(left * right);
            case DIVIDE -> {
                if (right == 0) {
                    return;
                }
                numbers.push(left / right);
            }
            default -> {
            }
        }
    }

    private boolean validTokenList(List<MathToken> tokenList) {
        if (tokenList.isEmpty()) return false;
        if (tokenList.get(tokenList.size() - 1).getToken() != ArithmeticToken.NUMBER) return false;
        if (tokenList.get(0).getToken() != ArithmeticToken.NUMBER) return false;

        for (int i = 0; i < tokenList.size() - 1; i++) {
            if (tokenList.get(i).equals(tokenList.get(i + 1))) {
                return false;
            }
            if ((tokenList.get(i).getToken() != ArithmeticToken.NUMBER) && (tokenList.get(i + 1).getToken() != ArithmeticToken.NUMBER)) {
                return false;
            }
        }
        return true;
    }

    private Number formatNumber(Number number) {
        if (number.intValue() == number.doubleValue()) {
            return number.intValue();
        }
        return number.doubleValue();
    }

    public double test(String prompt) {
        if (!containsExpression(prompt)) return Double.MAX_VALUE;
        prompt = prompt.replace("print(", "");
        prompt = prompt.replace(")", "");
        var list = lexer.lex(prompt);

        return evaluateExpression(list);
    }


    enum ArithmeticToken {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, NUMBER, MINUS
    }

    static class MathToken {

        private final ArithmeticToken token;
        private double val;

        MathToken(ArithmeticToken token) {
            this.token = token;
        }

        MathToken(ArithmeticToken token, double val) {
            this(token);
            this.val = val;
        }

        public double getVal() {
            return val;
        }

        public ArithmeticToken getToken() {
            return token;
        }

        @Override
        public String toString() {
            return token.toString() + "|" + val;
        }
    }

    static class Lexer {
        StringBuilder currentWord;
        ArrayList<MathToken> returnList;

        public List<MathToken> lex(String prompt) {
            currentWord = new StringBuilder();
            returnList = new ArrayList<>(prompt.length() / 3);
            var arr = prompt.toCharArray();
            for (char currentChar : arr) {
                switch (currentChar) {
                    case '+' -> {
                        parseNumber(currentWord.toString());
                        returnList.add(new MathToken(ArithmeticToken.ADD));
                    }
                    case '-' -> {
                        parseNumber(currentWord.toString());
                        returnList.add(new MathToken(ArithmeticToken.SUBTRACT));
                    }
                    case '*' -> {
                        parseNumber(currentWord.toString());
                        returnList.add(new MathToken(ArithmeticToken.MULTIPLY));
                    }
                    case '/' -> {
                        parseNumber(currentWord.toString());
                        returnList.add(new MathToken(ArithmeticToken.DIVIDE));
                    }
                    case ' ' -> {
                    }
                    default -> currentWord.append(currentChar);
                }
            }
            parseNumber(currentWord.toString());
            return returnList;
        }

        private void parseNumber(String num) {
            if (currentWord.isEmpty()) return;
            try {
                double number = Double.parseDouble(num);
                returnList.add(new MathToken(ArithmeticToken.NUMBER, number));
            } catch (NumberFormatException ignored) {
            }
            currentWord.setLength(0);
        }
    }
}

