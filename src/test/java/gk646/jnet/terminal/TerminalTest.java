package gk646.jnet.terminal;

import gk646.jnet.userinterface.terminal.Terminal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminalTest {

    static Terminal terminal = new Terminal();


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


    @Test
    void testCommands() {
        assertEquals("5", terminal.testCommand("2+3"));

        assertEquals("invalid arithmetic expr: 2+", terminal.testCommand("2+"));

        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder([3,3,3],sigmoid)"));

        assertEquals("hey", terminal.testCommand("print(hey)"));

        assertEquals("> ", terminal.testCommand(" "));

 }


    @Test
    void netBuilderTest() {
        assertEquals("added new list: he", terminal.testCommand("list he [2,3]"));

        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder([3,],relu)"));
        assertEquals("created new NetBuilder", terminal.testCommand("new NetBuilder($he,relu)"));
    }
}
