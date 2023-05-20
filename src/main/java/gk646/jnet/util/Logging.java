package gk646.jnet.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logging extends java.util.logging.Logger {
    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    public Logging(String name, String resourceBundleName) {
        super(name, resourceBundleName);
        this.setUseParentHandlers(false);
    }


    public boolean logException(Class<? extends Exception> exceptionClass, String text) {
        log(new LogRecord(Level.ALL, "[" + exceptionClass.getName() + "] " + text));
        return false;
    }
}
