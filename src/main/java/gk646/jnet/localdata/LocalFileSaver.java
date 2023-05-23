package gk646.jnet.localdata;

import gk646.jnet.Info;
import gk646.jnet.userinterface.terminal.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class LocalFileSaver {
    public enum OperatingSystem {
        WINDOWS, UNIX, UNSUPPORTED, MAC
    }

    LocalData localData;
    OperatingSystem os;
    final boolean savePossible;
    private String fullDirectoryPath;

    public LocalFileSaver() {
        os = getOS();
        savePossible = (isOSSupported() & canCreateAppDataFolder() & createLocalFiles());
        if (!savePossible) {
            Log.logger.warning("cant read user setting");
            return;
        }

        localData = readLocalFiles();
    }


    public void saveLocalFiles() {
        if (!savePossible || fullDirectoryPath == null) return;
    }


    public LocalData readLocalFiles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fullDirectoryPath + File.separator + Info.CONFIGURATION_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            return LocalData.empty();
        } catch (IOException e) {
            return LocalData.empty();
        }
    }

    private boolean canCreateAppDataFolder() {
        String appDataPath = null;
        switch (os) {
            case UNIX -> appDataPath = System.getenv("HOME");
            case WINDOWS -> appDataPath = System.getenv("APPDATA");
        }
        if (appDataPath == null) return false;

        fullDirectoryPath = appDataPath + File.separator + (os == OperatingSystem.UNIX ? "." + Info.APPLICATION_NAME.toLowerCase() : Info.APPLICATION_NAME);
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


    private boolean isOSSupported() {
        return os != OperatingSystem.UNSUPPORTED;
    }

    private OperatingSystem getOS() {
        String osString = System.getProperty("os.name");
        if (osString.equals("Linux")) {
            return OperatingSystem.UNIX;
        } else if (osString.equals("FreeBSD")) {
            return OperatingSystem.UNSUPPORTED;
        } else if (osString.equals("NetBSD")) {
            return OperatingSystem.UNSUPPORTED;
        } else if (osString.equals("OpenBSD")) {
            return OperatingSystem.UNSUPPORTED;
        } else if (osString.contains("Darwin") || osString.contains("OS X")) {
            return OperatingSystem.UNSUPPORTED;
        } else if (osString.startsWith("Windows")) {
            return OperatingSystem.WINDOWS;
        } else {
            return OperatingSystem.UNSUPPORTED;
        }
    }
}
