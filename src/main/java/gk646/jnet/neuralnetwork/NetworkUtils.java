package gk646.jnet.neuralnetwork;

import gk646.jnet.neuralnetwork.exceptions.NetworkIntegrityException;

import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class NetworkUtils {
    public static final Random rng = new Random(System.nanoTime());
    public static final Logger logger = Logger.getLogger(NetworkUtils.class.getName());
    /**
     * Controls the visibility of debug messages and logging.
     */
    public static boolean verbose = true;

    static {
        initLogger();
    }
    NetworkUtils() {
    }

    public static void removeHandler() {
        Handler[] handlers = logger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            logger.removeHandler(handlers[0]);
        }
    }

    public void print3DArray(float[][][] array) {
        if (!verbose) return;
        for (int i = 0; i < array.length; i++) {
            System.out.println("Weight Layer Pair " + i + ":");
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    float num = array[i][j][k];
                    if(num < 0){
                        System.out.printf("%.4f | ", array[i][j][k]);
                    }else {
                        System.out.printf(" %.4f | ", array[i][j][k]);
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }


    /**
     * Performs {@link Thread#sleep(long)} for the given time.
     * Skips the duration if  {@link NetworkUtils#verbose} is false:
     * @param millis amount of milliseconds as int.
     */
    public void sleep(int millis) {
        if(!verbose) return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public void printNetwork(Network network) {
        if (!verbose) return;
        StringBuilder sb = new StringBuilder("\n");
        int max = 0;
        for (int i = 0; i < network.layerCount; i++) {
            if (network.layerInfo[i] > max) {
                max = network.layerInfo[i];
            }
        }
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < network.layerCount; j++) {
                if (i < network.layerInfo[j]) {
                    sb.append("o ");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

    /**
     * Performs various checks regarding the structural integrity of the network.
     */
    void networkIntegrityCheck(Network network) {
        logger.info("Performing network integrity checks");
        for (int i = 0; i < network.layerCount - 1; i++) {
            if (network.weightMatrix[i].length != network.layerInfo[i] || network.weightMatrix[i][0].length != network.layerInfo[i + 1]) {
                logger.severe("Weight matrix dimensions don't match layerInfo!");
                throw new NetworkIntegrityException("Weight matrix dimensions don't match layerInfo!");
            }
        }
        logger.info("Printing network structure:");
        network.netUtils.printNetwork(network);
        network.netUtils.sleep(20);

        logger.info("Printing weight matrix structure: \n");
        network.netUtils.print3DArray(network.weightMatrix);
        network.netUtils.sleep(20);

        logger.info("Network integrity checks successful!\n");
    }


    private static void initLogger() {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }
        if (verbose) {
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
    }
}
