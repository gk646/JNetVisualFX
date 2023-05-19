package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.userinterface.terminal.commands.SettableProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandController {
    public static final Command[] COMMANDS = Command.values();
    public static final SettableProperties[] SETTABLE_PROPERTIES = SettableProperties.values();
    public static final List<String> helloList = new ArrayList<>(List.of("hello", "hey"));
    public static final List<String> exitList = new ArrayList<>(List.of("logout", "shutdown", "goodbye", "bye", "byebye", "cya"));
    public static final List<String> creatableObjects = new ArrayList<>(List.of("NetBuilder", "Network"));
    public static final List<String> settableProperties = Arrays.stream(SETTABLE_PROPERTIES).map(Enum::toString).toList();

    private CommandController() {
    }
}