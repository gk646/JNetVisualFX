package gk646.jnet.localdata.files;

import gk646.jnet.Info;
import gk646.jnet.userinterface.terminal.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserSettings {
    static final String DISCLAIMER = """
            This is the configuration file for JNetVisualFX. This file will be overridden each save to preserve the formatting.
            If it gets unreadable for some reason it will be reset to the default values.
            You can change values to your liking and they will be loaded on startup.
            """;

    static final String GENERAL = "[GENERAL]";
    public Map<String, Number> settingsMap;

    public UserSettings() {
        settingsMap = new HashMap<>(10);

        settingsMap.put("fontsize", 15);
        settingsMap.put("circlesize", 20);
    }

    public void readLine(String line) {
        String[] keyValuePair = line.split(": ");
        if (keyValuePair.length < 2) return;

        for (String key : settingsMap.keySet()) {
            if (key.equals(keyValuePair[0])) {
                settingsMap.put(key, Double.parseDouble(keyValuePair[1]));
                return;
            }
        }
    }

    public void save(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + File.separator + Info.CONFIGURATION_FILE_NAME))) {
            bw.write(DISCLAIMER);
            bw.newLine();
            bw.newLine();

            bw.write(GENERAL);
            bw.newLine();

            var set = settingsMap.entrySet();
            for (Map.Entry<String, Number> pair : set) {
                bw.write(pair.getKey() + ": " + pair.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            Log.logger.logException(IOException.class, "error saving to disk");
        }
    }



}
