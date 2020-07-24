package cl.dreamit.elevateit.Utils;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private final static Logger LOGGER =
                Logger.getLogger(CONF.LOG_NAME);

    public static void error(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message + " | " + e.toString());
        new LogInterno(message + " | " + e.toString()).save();
    }

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
        new LogInterno(message, Level.SEVERE).save();
    }

    public static void error(String format, Object... args) {
        String mensaje = String.format(format, args);
        error(mensaje);
    }

    public static void error(HashMap<Integer, ?> hashMap) {
        for (Map.Entry<Integer, ?> entry : hashMap.entrySet()) {
            LOGGER.log(Level.SEVERE, entry.getKey() + " => " + entry.getValue().toString());
        }
    }

    public static void errorBdData(HashMap<String, HashMap<String, Object>> hashMap) {
        for (Map.Entry<String, HashMap<String, Object>> map : hashMap.entrySet()) {
            String line = "";
            for (Map.Entry<String, Object> entry : map.getValue().entrySet()) {
                line += entry.getKey() + " = " + entry.getValue() + " | ";
            }
            LOGGER.log(Level.SEVERE, "| id = " + map.getKey() + " | " + line);
        }
    }

    public static void info(String s) {
        LOGGER.log(Level.INFO, s);
        new LogInterno(s).save();
    }
}
