package gk646.jnet.neuralNetwork;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class NetworkUtils {
    public static final Logger logger = Logger.getLogger(Network.class.getName());

    static {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return
                        " [" + logRecord.getLevel() + "] " +
                                logRecord.getMessage() + "\n";
            }
        });


        logger.addHandler(consoleHandler);
    }
    NetworkUtils(){

    }

    public void print3DArray(short[][][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println("Slice " + i + ":");
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    System.out.print(array[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
