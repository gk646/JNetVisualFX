package gk646.jnet.localdata.files;

import gk646.jnet.Info;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.util.NumberUtil;
import gk646.jnet.util.StringUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public final class UserStatistics {
    public static final Map<Stat, Integer> localNumbers = new EnumMap<>(Stat.class);
    private static final String DISCLAIMER = """
            *-------------------------------------------*
            This is an automatically generated document; Do not edit!
            If the information cannot be read, no further statistics will be saved to prevent deletion.
            """;
    private static final String DISCLAIMER_UNREADABLE = """
            *------------------ERROR-------------------*
            THIS FILE IS UNREADABLE! NO FURTHER STATISTICS ARE SAVED!
            USE THE COMMAND: "reset-user-statistics" TO RESET THEM.
            """;
    private static final int SKIP_COUNT = StringUtil.countChar(DISCLAIMER, '\n') + 1;
    public static boolean unreadableStatistics;
    private int readCounter = 0;

    public UserStatistics() {
        localNumbers.put(Stat.netBuilderCustomizations, 0);
        localNumbers.put(Stat.networksTrained, 0);
        localNumbers.put(Stat.arraysCreated, 0);
        localNumbers.put(Stat.numberOfForwardPasses, 0);
        localNumbers.put(Stat.numberOfBackPropagations, 0);
        localNumbers.put(Stat.totalSecondsUsed, 0);
        localNumbers.put(Stat.totalCommandsUsed, 0);
        localNumbers.put(Stat.exercisesFinished, 0);
    }

    public static void updateStat(Stat statName, int value) {
        localNumbers.put(statName, localNumbers.get(statName).intValue() + value);
    }

    public static void replaceStat(Stat stat, int value) {
        localNumbers.put(stat, value);
    }

    public static Number getStat(Stat statName) {
        return localNumbers.get(statName);
    }

    public void readLine(String line) {
        if (readCounter < SKIP_COUNT) {
            readCounter++;
            return;
        }

        String[] keyValuePair = line.split(": ");

        for (Stat key : localNumbers.keySet()) {
            if (key.name().equals(keyValuePair[0])) {
                localNumbers.put(key, Integer.parseInt(keyValuePair[1]));
                return;
            }
        }
    }

    public void save(String path) {
        if (unreadableStatistics) {
            addUnreadableTag(path);
            return;
        }


        replaceStat(Stat.totalSecondsUsed, NumberUtil.getNewTotalTime(localNumbers.get(Stat.totalSecondsUsed)));


        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + File.separator + Info.USER_STATISTIC_FILE_NAME))) {
            bw.write(DISCLAIMER);
            bw.newLine();

            var set = localNumbers.entrySet();
            for (Map.Entry<Stat, Integer> pair : set) {
                bw.write(pair.getKey() + ": " + pair.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            Log.logger.logException(IOException.class, "error saving to disk");
        }
    }

    private void addUnreadableTag(String path) {
        File file = new File(path + File.separator + Info.USER_STATISTIC_FILE_NAME);

        if (file.length() > 1000) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.newLine();
            bw.newLine();
            bw.write(DISCLAIMER_UNREADABLE);
        } catch (IOException e) {
            Log.logger.logException(IOException.class, "error adding unreadable tag");
        }
    }

    public void reset(String fullPath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath + File.separator + Info.USER_STATISTIC_FILE_NAME))) {
            bw.write(DISCLAIMER);
            bw.newLine();

            var set = localNumbers.entrySet();
            for (Map.Entry<Stat, Integer> pair : set) {
                bw.write(pair.getKey() + ": " + pair.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            Log.logger.logException(IOException.class, "error resetting user-statistics");
        }
        if (unreadableStatistics) {
            unreadableStatistics = false;
        }
    }

    public enum Stat {
        netBuilderCustomizations, networksTrained, arraysCreated, numberOfForwardPasses, numberOfBackPropagations, totalSecondsUsed, totalCommandsUsed, exercisesFinished
    }
}
