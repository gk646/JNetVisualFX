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
        int commandHistoryOffset = 0;
        int previousSize = 0;


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
        System.out.println(Terminal.commandHistory);
        Terminal.commandHistory.add("2");
        assertEquals("help", Terminal.commandHistory.get(commandHistoryOffset));
    }
}
