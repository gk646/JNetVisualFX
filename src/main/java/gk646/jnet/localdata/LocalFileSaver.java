package gk646.jnet.localdata;

import gk646.jnet.Info;
import gk646.jnet.userinterface.terminal.Log;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class LocalFileSaver {
    final OperatingSystemMXBean os;
    final boolean savePossible;
    private String fullDirectoryPath;

    public LocalFileSaver() {
        os = ManagementFactory.getOperatingSystemMXBean();
        savePossible = (isOSSupported(os.getName()) & canCreateAppDataFolder() & createLocalFiles());
        if (!savePossible) {
            Log.logger.warning("no local save possible");
        } else {
            Log.logger.info("user settings read");
        }
    }

    private boolean canCreateAppDataFolder() {
        String appDataPath = System.getenv("APPDATA");
        if (appDataPath == null) return false;

        fullDirectoryPath = appDataPath + File.separator + Info.APPLICATION_NAME;
        File directory = new File(fullDirectoryPath);
        if (directory.exists()) {
            return true;
        } else {
            return directory.mkdir();
        }
    }

    private boolean createLocalFiles() {
        File config = new File(fullDirectoryPath + File.separator + Info.CONFIGURATION_FILE_NAME);
        File stats = new File(fullDirectoryPath + File.separator + Info.USER_STATISTIC_FILE_NAME);

        try {
            if (config.exists() & stats.exists()) return true;
            return (config.createNewFile() & stats.createNewFile());
        } catch (IOException e) {
            return false;
        }
    }

    public void saveLocalFiles() {
        if (!savePossible || fullDirectoryPath == null) return;
    }

    private boolean isOSSupported(String osName) {
        for (String supportedOS : Info.SUPPORTED_OS) {
            if (osName.contains(supportedOS)) return true;
        }
        return false;
    }
}
