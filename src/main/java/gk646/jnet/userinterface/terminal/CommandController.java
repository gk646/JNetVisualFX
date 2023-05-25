package gk646.jnet.userinterface.terminal;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.userinterface.terminal.commands.CreatableObjects;
import gk646.jnet.userinterface.terminal.commands.SettableProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandController {
    public static final Command[] COMMANDS = Command.values();
    public static final SettableProperties[] SETTABLE_PROPERTIES = SettableProperties.values();
    public static final CreatableObjects[] CREATABLE_OBJECTS = CreatableObjects.values();
    public final static List<String> MANUALS = new ArrayList<>(25);
    static final UserStatistics.Stat[] USER_STATS = UserStatistics.Stat.values();
    static final List<String> creatableObjects = Arrays.stream(CREATABLE_OBJECTS).map(Enum::toString).toList();
    static final List<String> settableProperties = Arrays.stream(SETTABLE_PROPERTIES).map(Enum::toString).toList();
    static final List<String> userStatistics = Arrays.stream(USER_STATS).map(Enum::toString).toList();
    static final ArrayList<String> commandList = new ArrayList<>(Arrays.stream(CommandController.COMMANDS).map(Enum::toString).toList());

    static {
        MANUALS.addAll(commandList);
        MANUALS.addAll(creatableObjects);
        MANUALS.addAll(settableProperties);
    }

    private CommandController() {

    }
}
