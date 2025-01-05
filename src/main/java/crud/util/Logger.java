package crud.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_DIR = "logs";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public enum Level {
        INFO, WARNING, ERROR, DEBUG
    }

    public static void log(String className, String message, Level level) {
        String fileName = LOG_DIR + File.separator +
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";

        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = DATE_FORMAT.format(new Date());
            String logMessage = String.format("[%s] %s [%s] %s",
                    timestamp, level, className, message);

            pw.println(logMessage);

            // Also print to console for development
            System.out.println(logMessage);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public static void info(String className, String message) {
        log(className, message, Level.INFO);
    }

    public static void warning(String className, String message) {
        log(className, message, Level.WARNING);
    }

    public static void error(String className, String message) {
        log(className, message, Level.ERROR);
    }

    public static void debug(String className, String message) {
        log(className, message, Level.DEBUG);
    }
}