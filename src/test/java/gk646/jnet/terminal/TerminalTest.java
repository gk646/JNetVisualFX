package gk646.jnet.terminal;

import gk646.jnet.userinterface.terminal.Terminal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminalTest {


    @Test
    void testCursorShift() {
        Terminal.addText("hello");
        Terminal.currentText = new StringBuilder("hey");

        for (int i = 0; i < 3; i++) {
            //Simulate LEFT press
            if (Terminal.cursorOffsetLeft < Terminal.currentText.length()) {
                Terminal.cursorOffsetLeft++;
            }
        }

        int length = Terminal.currentText.length();
        if (length > 0) {
            if (Terminal.cursorOffsetLeft == 0) {
                Terminal.currentText.deleteCharAt(length - 1);
            } else if (Terminal.cursorOffsetLeft != length) {
                Terminal.currentText.deleteCharAt(length - 1 - Terminal.cursorOffsetLeft);
            }
        }
    }

    @Test
    void testCommandHistory() {
        Terminal.commandHistory.clear();
        int commandHistoryOffset = 0;
        int previousSize = Terminal.commandHistory.size();
        //Simulate input
        Terminal.parseText("print(hey)");

        assertEquals(previousSize + 1, Terminal.commandHistory.size());

        //Simulate UP press
        if (commandHistoryOffset < Terminal.commandHistory.size()) {
            commandHistoryOffset++;
        }

        assertEquals("print(hey)", Terminal.commandHistory.get(commandHistoryOffset));

        //Simulate input
        Terminal.parseText("help");

        assertEquals(previousSize + 2, Terminal.commandHistory.size());

        //Simulate UP press
        if (commandHistoryOffset < Terminal.commandHistory.size() - 1) {
            commandHistoryOffset++;
        }
        Terminal.commandHistory.add("2");
        assertEquals("help", Terminal.commandHistory.get(commandHistoryOffset));
    }

/*
    @Test
    void testCommands() {
        var terminal = new Terminal();
        assertEquals("5", terminal.testCommand("2+3"));

        assertEquals("invalid arithmetic expr: 2+", terminal.testCommand("2+"));

        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder([3,3,3],sigmoid)"));

        assertEquals("hey", terminal.testCommand("print(hey)"));

        assertEquals("> ", terminal.testCommand(" "));

 }
 */

    @Test
    void netBuilderTest() {
        var terminal = new Terminal();
        assertEquals("added new list: he", terminal.testCommand("list he [2,3]"));

        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder([3,],relu)"));
        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder($he,relu)"));
    }
}
