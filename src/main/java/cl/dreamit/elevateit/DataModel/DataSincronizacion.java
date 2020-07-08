package cl.dreamit.elevateit.DataModel;

import java.util.List;
import java.util.Map;

/**
 * Clase que representa una parte de la respuesta de sincronizacion de datos de la API. Particularmente una tabla, y los datos nuevos.
 */
public class DataSincronizacion {
    public String tabla;
    public List<Map<String, Object>> data;
}
