package cl.dreamit.elevateit.Utils;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que tiene la logica de los logs
 */
public class Log {

    private final static Logger LOGGER =
                Logger.getLogger(CONF.LOG_NAME);
    /**
     * Encargado de mostrar un log de error y registrarlo en la base de datos interna
     *
     * @param message Mensaje de error
     * @param e       Excepcion obtenida del error
     */
    public static void error(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message + " | " + e.toString());
        new LogInterno(message + " | " + e.toString()).save();
    }

    /**
     * Encargado de mostrar un log de error y registrarlo en la base de datos interna
     *
     * @param message Mensaje de error
     */
    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
        new LogInterno(message).save();
    }

    public static void error(String format, Object... args) {
        String mensaje = String.format(format, args);
        error(mensaje);
    }

    /**
     * Muestra el por consola los datos de un HashMap
     *
     * @param hashMap HashMap con informacion
     */
    public static void error(HashMap<Integer, ?> hashMap) {
        for (Map.Entry<Integer, ?> entry : hashMap.entrySet()) {
            LOGGER.log(Level.SEVERE, entry.getKey() + " => " + entry.getValue().toString());
        }
    }

    /**
     * Muestra el por consola los datos de un HashMap, el HashMap tiene la estructura establecida para los modelos de la BD
     *
     * @param hashMap HashMap con informacion
     */
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
