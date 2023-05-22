package gk646.jnet.terminal;

import gk646.jnet.userinterface.terminal.Terminal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayParserTests {


    @Test
    void arrayParseTests() {
        var terminal = new Terminal();

        // Single and multi-digit integers in one-dimensional array
        assertEquals("added new list: test1", terminal.testCommand("list test1 [12,345,6789]"));
        assertEquals("updated list: test1", terminal.testCommand("list test1 [1,2,3,4,5,6,7,8,9]"));

        // Decimal numbers in one-dimensional array
        assertEquals("added new list: test2", terminal.testCommand("list test2 [23.45,67.89]"));
        assertEquals("updated list: test2", terminal.testCommand("list test2 [0.123,4.567,8.9]"));

        // Empty list update
        assertEquals("empty list added: test3", terminal.testCommand("list test3 []"));
        assertEquals("updated list: test3", terminal.testCommand("list test3 [1]"));

        // Two-dimensional arrays
        assertEquals("added new list: test4", terminal.testCommand("list test4 [[1,2],[3,4]]"));
        assertEquals("updated list: test4", terminal.testCommand("list test4 [[5,6],[7,8]]"));

        // Two-dimensional arrays with different lengths for inner arrays
        assertEquals("added new list: test5", terminal.testCommand("list test5 [[1,2],[3,4,5]]"));
        assertEquals("updated list: test5", terminal.testCommand("list test5 [[6],[7,8,9]]"));

        // Invalid array declaration - unbalanced brackets
        assertEquals("invalid array declaration:[1,2,3,4", terminal.testCommand("list test6 [1,2,3,4"));
        assertEquals("invalid array declaration:1,2,3,4]", terminal.testCommand("list test6 1,2,3,4]"));

        // Array depth exceeding 2
        assertEquals("array depth exceeds 2:[[[1,2],[3,4]]]", terminal.testCommand("list test7 [[[1,2],[3,4]]]"));

        // Invalid array declaration - mixing dimensions
        assertEquals("invalid array declaration:[1,[2,3]]", terminal.testCommand("list test8 [1,[2,3]]"));
        assertEquals("invalid array declaration:[[1,2],3]", terminal.testCommand("list test8 [[1,2],3]"));
        assertEquals("invalid array declaration:[[1,2],3]]", terminal.testCommand("list test8 [[1,2],3]]"));

        assertEquals("invalid array declaration:[[1,2],3]", terminal.testCommand("list test8 [[1,2],3]"));

        assertEquals("invalid array declaration:[1,2],[3,4]]", terminal.testCommand("list test8 [1,2],[3,4]]"));

    }
}
