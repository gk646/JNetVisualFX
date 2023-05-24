package gk646.jnet.util;

import gk646.jnet.userinterface.terminal.Log;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogHandler extends Handler {

    /**
     * Publish a {@code LogRecord}.
     * <p>
     * The logging request was made initially to a {@code Logger} object,
     * which initialized the {@code LogRecord} and forwarded it here.
     * <p>
     * The {@code Handler}  is responsible for formatting the message, when and
     * if necessary.  The formatting should include localization.
     *
     * @param record description of the log event. A null record is
     *               silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            Log.addLogText(getFormatter().format(record));
        }
    }

    /**
     * Flush any buffered output.
     */
    @Override
    public void flush() {

    }

    /**
     * Close the {@code Handler} and free all associated resources.
     * <p>
     * The close method will perform a {@code flush} and then close the
     * {@code Handler}.   After close has been called this {@code Handler}
     * should no longer be used.  Method calls may either be silently
     * ignored or may throw runtime exceptions.
     *
     * @throws SecurityException if a security manager exists and if
     *                           the caller does not have {@code LoggingPermission("control")}.
     */
    @Override
    public void close() throws SecurityException {

    }

    @Override
    public Formatter getFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return
                        "[" + logRecord.getLevel() + "] " +
                                logRecord.getMessage();
            }
        };
    }

    @Override
    public Level getLevel() {
        return Level.ALL;
    }
}
