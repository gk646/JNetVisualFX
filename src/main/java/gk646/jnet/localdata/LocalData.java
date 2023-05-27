package gk646.jnet.localdata;

import gk646.jnet.Info;
import gk646.jnet.localdata.files.UserSettings;
import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.terminal.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class LocalData {

    final UserSettings settings;
    final UserStatistics stats;

    LocalData(UserStatistics stats, UserSettings settings) {
        this.settings = settings;
        this.stats = stats;
    }

    public static LocalData readLocalFiles(String fullDirectoryPath) {
        UserStatistics stats = readStats(fullDirectoryPath);
        UserSettings settings = readSettings(fullDirectoryPath);

        return new LocalData(stats, settings);
    }

    private static UserSettings readSettings(String fullDirectoryPath) {
        var settings = new UserSettings();
        try (BufferedReader reader = new BufferedReader(new FileReader(fullDirectoryPath + File.separator + Info.CONFIGURATION_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                settings.readLine(line);
            }
        } catch (IOException | NumberFormatException e) {
            Log.logger.warning("error reading user-config");
            return settings;
        }
        Log.logger.info("successfully read user-config");
        return settings;
    }

    private static UserStatistics readStats(String fullDirectoryPath) {
        var stats = new UserStatistics();
        try (BufferedReader reader = new BufferedReader(new FileReader(fullDirectoryPath + File.separator + Info.USER_STATISTIC_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stats.readLine(line);
            }
        } catch (IOException | NumberFormatException e) {
            Log.logger.warning("unreadable user-stats! use \"reset-user-statistics\" to reset");
            UserStatistics.unreadableStatistics = true;
            return stats;
        }
        Log.logger.info("successfully read user-statistics");
        return stats;
    }

    public void updateFontSize(int fontSize) {
        settings.settingsMap.put("fontsize", fontSize);
    }

    public void saveAll(String path) {
        if (settings != null) settings.save(path);
        if (stats != null) stats.save(path);
    }

    public void resetStats(String fullPath) {
        stats.reset(fullPath);
    }
}
