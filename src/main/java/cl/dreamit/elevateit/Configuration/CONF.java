package cl.dreamit.elevateit.Configuration;

/**
 * Se encarga de almacenar todas las variables de configuracion que usa el sistema
 */
public class CONF {
    public static final String SOFTWARE_TAG = "FULL-ACCESS";

    /****************** VARIABLES QUE DETERMINAN TIEMPO ******************/

    // Tiempo en que ejecutara un nuevo ciclo de sincronización
    public static int TIME_SYNC = 2000;

    // Tiempo que se mostrar un mensaje de error
    public final static int TIME_DIALOG_ERROR = 3000;

    // Tiempo que se mostrar el mensaje de éxito
    public final static int TIME_DIALOG_SUCCESS = 3000;

    // Tiempo que se mostrara la pantalla inicial "SPLASH SCREEN"
    public final static int TIME_SPLASH_SCREEN = 2000;

    // Tiempo en que se ejecutara un PING de estado
    public final static int TIME_CHECK_STATE = 5000;

    //Ruta de la API. esto está almacenado en el archivo Gradle 'build.grade' del módulo Full-Access.
    //Por defecto Development apunta a una ruta 'http://api.gk2-test', y en release cambia a 'https://gk2-api.gestkontrol.cl/'
    public final static String API_URL = "http://192.168.1.89:8080/";

    // Ruta con la cual se registra y obtiene el API Token
    public final static String URL_REGISTER_DEVICE = "/androidFullAccess/register";

    // Ruta que utiliza para la sincronizacion de datos hacia la nube mediante el Api de GK2
    public final static String URL_SYNC_DATA = "androidFullAccess/syncData";

    /****************** VERSIONES ******************/

    // Versión de la base de datos del configuraciones
    public final static int VERSION_DATA_BASE = 1;

    // Version del software, 1.0 es la inicial cada vez que exista una versión estable nueva modificar este valor
    public final static double VERSION_FULL_ACCESS = 1.0;


    // Cantidad de canales de control de acceso.
    public final static int CANTIDAD_CANALES = 16;

    // Si el configuraciones captura huellas o no.
    public final static int CAPTURA_HUELLAS = 0;

    // Tiempo de apertura por defecto en milisegundos
    public final static int DEFAULT_OPEN_TIME = 5000;
}
