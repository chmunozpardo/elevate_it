package cl.dreamit.elevateit.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Se encarga de almacenar todas las variables de configuracion que usa el sistema
 */
public class CONF {
    public static final String SOFTWARE_TAG = "FULL-ACCESS";

    /****************** VARIABLES QUE DETERMINAN TIEMPO ******************/

    // Tiempo en que ejecutara un nuevo ciclo de sincronización
    public static int TIME_SYNC = 1000;

    // Tiempo en que se ejecutara un PING de estado
    public final static int TIME_CHECK_STATE = 5000;

    //Ruta de la API. esto está almacenado en el archivo Gradle 'build.grade' del módulo Full-Access.
    //Por defecto Development apunta a una ruta 'http://api.gk2-test', y en release cambia a 'https://gk2-api.gestkontrol.cl/'
    public final static String API_URL = "http://192.168.1.89:8080/";

    // Ruta con la cual se registra y obtiene el API Token
    public final static String URL_REGISTER_DEVICE = "controladorElevateIT/register";

    // Ruta que utiliza para la sincronizacion de datos hacia la nube mediante el Api de GK2
    public final static String URL_SYNC_DATA = "controladorElevateIT/syncData";

    /****************** VERSIONES ******************/

    public final static String LOG_NAME = "cl.dreamit.elevateit";

    // Versión de la base de datos del configuraciones
    public final static int VERSION_DATA_BASE = 1;

    // Version del software, 1.0 es la inicial cada vez que exista una versión estable nueva modificar este valor
    public final static double VERSION_FULL_ACCESS = 1.0;


    // Cantidad de canales de control de acceso.
    public static int CANTIDAD_CANALES = 16;

    // Si el configuraciones captura huellas o no.
    public final static int CAPTURA_HUELLAS = 0;

    // Tiempo de apertura por defecto en milisegundos
    public final static int DEFAULT_OPEN_TIME = 5000;

    public final static String MODEL = "elevateIT";

    public final static String SERIE = "NanopiNeoPlus2";

    // Lista de controladores I2C para los relés
    public final static List<Integer> I2C_ADDRESSES = Arrays.asList(0x20);
}
