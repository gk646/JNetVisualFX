package gk646.jnet.userinterface.terminal;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.commands.Command;
import gk646.jnet.userinterface.terminal.commands.CreatableObjects;
import gk646.jnet.userinterface.terminal.commands.SettableProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandController {
    public static final Command[] COMMANDS = Command.values();

    private CommandController() {

    }
}
