package gk646.jnet.terminal;

import gk646.jnet.util.parser.ArithmeticParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArithmeticParserTest {

    final ArithmeticParser mathParser = new ArithmeticParser();

    @Test
    void testValidExpressions() {
        assertEquals(5.0, mathParser.test("3 + 2"));
        assertEquals(1.0, mathParser.test("5 - 4"));
        assertEquals(50.0, mathParser.test("10 * 5"));
        assertEquals(4.0, mathParser.test("20 / 5"));
        assertEquals(14.0, mathParser.test("2 + 3 * 4"));
        assertEquals(2.0, mathParser.test("10 / 2 - 3"));
        assertEquals(9.0, mathParser.test("3 + 2 * 4 - 6 / 3"));
    }

    @Test
    void testInvalidExpressions() {
        assertEquals(Double.NaN, mathParser.test("3 + "));
        assertEquals(Double.NaN, mathParser.test("5 -"));
        assertEquals(Double.NaN, mathParser.test("* 5"));
        assertEquals(Double.NaN, mathParser.test("/ 5"));
        assertEquals(Double.NaN, mathParser.test("2 + * 4"));
        assertEquals(Double.NaN, mathParser.test("10 / - 3"));
        assertEquals(Double.NaN, mathParser.test("+ 2 * 4 - 6 / "));
        assertEquals(Double.NaN, mathParser.test("3 + a * 4"));
        assertEquals(Double.MAX_VALUE, mathParser.test("10 $ 2"));
        assertEquals(Double.MAX_VALUE, mathParser.test("abc"));
        assertEquals(Double.MAX_VALUE, mathParser.test(" "));
    }


}
