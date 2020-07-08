package cl.dreamit.elevateit.Utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SoftwareLogger {
    private final static Logger LOGGER =
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void logInfo(String infoString) {
        LOGGER.log(Level.INFO, infoString);
    }
    public void logError(String infoString) {
        LOGGER.log(Level.SEVERE, infoString);
    }
    public void logWarning(String infoString) {
        LOGGER.log(Level.WARNING, infoString);
    }
    public void logConfig(String infoString) {
        LOGGER.log(Level.CONFIG, infoString);
    }
}