package gk646.jnet.userinterface.terminal;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.userinterface.terminal.commands.CreatableObjects;
import gk646.jnet.userinterface.terminal.commands.SettableProperties;

import java.util.Arrays;
import java.util.List;

public final class CommandController {
    public static final Command[] COMMANDS = Command.values();
    public static final SettableProperties[] SETTABLE_PROPERTIES = SettableProperties.values();
    public static final CreatableObjects[] CREATABLE_OBJECTS = CreatableObjects.values();
    public static final UserStatistics.Stat[] USER_STATS = UserStatistics.Stat.values();
    public static final List<String> creatableObjects = Arrays.stream(CREATABLE_OBJECTS).map(Enum::toString).toList();
    public static final List<String> settableProperties = Arrays.stream(SETTABLE_PROPERTIES).map(Enum::toString).toList();

    public static List<String> userStatistics = Arrays.stream(USER_STATS).map(Enum::toString).toList();

    private CommandController() {

    }
}
