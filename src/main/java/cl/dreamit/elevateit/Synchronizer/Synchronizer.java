package cl.dreamit.elevateit.Synchronizer;

import cl.dreamit.elevateit.AccessControl.ProcesadorComandosManuales;
import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.DataSincronizacionSubida;
import cl.dreamit.elevateit.DataModel.RespuestaRegistroGK;
import cl.dreamit.elevateit.DataModel.RespuestaSincronizacion;
import cl.dreamit.elevateit.DataModel.Const.Parametro;
import cl.dreamit.elevateit.DataModel.Const.SYNC_ERROR;
import cl.dreamit.elevateit.DataModel.DAO.BloquesCanalesHorarios;
import cl.dreamit.elevateit.DataModel.DAO.CanalesHorarios;
import cl.dreamit.elevateit.DataModel.DAO.ComandosManuales;
import cl.dreamit.elevateit.DataModel.DAO.Configuraciones;
import cl.dreamit.elevateit.DataModel.DAO.ConjuntosReservas;
import cl.dreamit.elevateit.DataModel.DAO.Controladores;
import cl.dreamit.elevateit.DataModel.DAO.Estacionamientos;
import cl.dreamit.elevateit.DataModel.DAO.LogsAcceso;
import cl.dreamit.elevateit.DataModel.DAO.LogsInternos;
import cl.dreamit.elevateit.DataModel.DAO.ParametrosControladores;
import cl.dreamit.elevateit.DataModel.DAO.ParametrosPuntosAccesos;
import cl.dreamit.elevateit.DataModel.DAO.Personas;
import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;
import cl.dreamit.elevateit.DataModel.DAO.Reservas;
import cl.dreamit.elevateit.DataModel.DAO.ReservasValidadas;
import cl.dreamit.elevateit.DataModel.DAO.RespuestasComandos;
import cl.dreamit.elevateit.DataModel.DAO.TarjetasAcceso;
import cl.dreamit.elevateit.DataModel.DAO.UploadableDAO;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.Configuracion;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.UploadableEntity;
import cl.dreamit.elevateit.DataModel.Entities.GK2.CanalHorario;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ConjuntoReserva;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Controlador;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Estacionamiento;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ParametroControlador;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ParametroPuntoAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Persona;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.Hardware.Relay;
import cl.dreamit.elevateit.Utils.HttpRequest;
import cl.dreamit.elevateit.Utils.Log;
import cl.dreamit.elevateit.Utils.NetworkUtil;
import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.Utils.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;

import com.google.gson.Gson;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Synchronizer implements Runnable {
    private String targetDatabase = null;
    private String apiToken = null;
    private String apiURL = null;
    private Long lastSyncTimestamp;
    private List<UploadableDAO> tablasSubida = Arrays.asList(
        LogsAcceso.INSTANCE,
        RespuestasComandos.INSTANCE,
        ReservasValidadas.INSTANCE
    );

    public Synchronizer() {
        Configuracion token = Configuraciones.INSTANCE.getParametro("apitoken");
        Configuracion database = Configuraciones.INSTANCE.getParametro("base_datos");
        Configuracion url = Configuraciones.INSTANCE.getParametro("API_URL");
        if(token == null) {
            Log.error("No existe apitoken. Se debe registrar el dispositivo.");
            System.exit(0);
        } else if(database == null) {
            Log.error("No existe base_datos. Se debe registrar el dispositivo.");
            System.exit(0);
        } else if(url == null) {
            Log.error("No existe API_RUL. Se debe registrar el dispositivo.");
            System.exit(0);
        }
        apiToken = token.valor;
        targetDatabase = database.valor;
        apiURL = url.valor;
    }

    public void run() {
        Configuracion timeStamp = Configuraciones.INSTANCE.getParametro("lastSyncTimestamp");
        if(timeStamp == null){
            timeStamp = new Configuracion();
            timeStamp.parametro = "lastSyncTimestamp";
            timeStamp.valor = "0";
            Configuraciones.INSTANCE.save(timeStamp);
        }
        lastSyncTimestamp = Long.parseLong(timeStamp.valor);
        while(true){
            if(SyncControl.INSTANCE.getState()){
                try{
                    Thread cleanThread = new Thread(()-> {
                        LogsAcceso.INSTANCE.clean();
                        LogsInternos.INSTANCE.clean();
                    });
                    cleanThread.start();
                    Thread.sleep(CONF.TIME_SYNC);
                    cleanThread.join();
                } catch (InterruptedException ex){}
                SyncMessage message = new SyncMessage(lastSyncTimestamp);
                Map<String, Long> ultimosID = cargarDatos(message);
                Map<String, Object> postData = new HashMap<>();
                postData.put("api_token", apiToken);
                postData.put("nombreInstancia", targetDatabase);
                postData.put("syncData", message);
                postData.put("fechaValidez", Util.getDateTime(new Date()));

                HttpRequest request = new HttpRequest(apiURL + CONF.URL_SYNC_DATA, postData);
                String respuesta = request.getResponse();
                try {
                    RespuestaSincronizacion syncResponse = new Gson().fromJson(respuesta, RespuestaSincronizacion.class);
                    if (syncResponse.estado.equals("OK")) {
                        lastSyncTimestamp = syncResponse.currentTimestamp;
                        TarjetasAcceso.INSTANCE.save(syncResponse.tarjetas);
                        almacenarReservas(syncResponse.reservas);
                        almacenarControlador(syncResponse.controlador);
                        almacenarCanalesHorarios(syncResponse.canalesHorarios);
                        ProcesadorComandosManuales.INSTANCE.procesarComandos(syncResponse.comandos);
                        ComandosManuales.INSTANCE.save(syncResponse.comandos);
                        actualizarUltimosID(ultimosID);
                    } else if (syncResponse.estado.equals("ERROR")) {
                        switch (syncResponse.error) {
                            case "":
                                Log.error("Error no especificado, revisar plataforma.");
                                System.exit(0);
                                break;
                            case SYNC_ERROR.INVALID_API_TOKEN:
                                Log.error("El API Token no es válido.");
                                System.exit(0);
                                break;
                        }
                    }
                } catch (Exception ex) {
                    Log.error("Error decodificando JSON: " + ex.getMessage());
                    Log.error("Consulta API: " + request.toString());
                    Log.error("Respuesta: " + respuesta);
                    ex.printStackTrace();
                }
            }
        }
    }

    private void almacenarCanalesHorarios(CanalHorario[] canalesHorarios) {
        if (canalesHorarios == null) {
            return;
        }
        CanalesHorarios.INSTANCE.save(canalesHorarios);
        for (CanalHorario c : canalesHorarios) {
            if (c.bloques_horario_trashed != null) {
                BloquesCanalesHorarios.INSTANCE.save(c.bloques_horario_trashed);
            }
        }
    }

    private void almacenarControlador(Controlador controlador) {
        if (controlador == null) {
            return;
        }
        //Primero se almacenan los controladores.
        Controladores.INSTANCE.save(controlador);
        //Luego guardamos las estructuras anidadas.
        List<ParametroControlador> parametrosControlador = new ArrayList<>();
        List<PuntoAcceso> puntosAccesos = new ArrayList<>();
        List<ParametroPuntoAcceso> parametrosPuntosAccesos = new ArrayList<>();
        if (controlador.parametros_controlador != null) {
            parametrosControlador.addAll(Arrays.asList(controlador.parametros_controlador));
            actualizarParametrosControlador(controlador.parametros_controlador);
        }
        if (controlador.punto_acceso != null) {
            for (PuntoAcceso p : controlador.punto_acceso) {
                puntosAccesos.add(p);
                if (p.parametros_punto_acceso != null) {
                    parametrosPuntosAccesos.addAll(Arrays.asList(p.parametros_punto_acceso));
                    actualizarParametrosPuntoAcceso(p.parametros_punto_acceso);
                }
            }
        }
        PuntosAccesos.INSTANCE.save(puntosAccesos);
        ParametrosControladores.INSTANCE.save(parametrosControlador);
        ParametrosPuntosAccesos.INSTANCE.save(parametrosPuntosAccesos);
    }

    private void actualizarParametrosControlador(ParametroControlador[] parametros_controlador) {
        for (ParametroControlador p : parametros_controlador) {
            if (p.valor_override == null) {
                continue;
            }
        }
    }

    private void actualizarParametrosPuntoAcceso(ParametroPuntoAcceso[] parametros_punto_acceso) {
        for (ParametroPuntoAcceso p : parametros_punto_acceso) {
            if ("contactoApertura".equals(p.parametro)) {
                //ExitButton.setTipoContacto(p.valor_override != null ? p.valor_override : p.valor);
            }
        }
    }

    private Map<String, Long> cargarDatos(SyncMessage sm) {
        Map<String, Long> ultimosID = new HashMap<>();
        for (int i = 0; i < tablasSubida.size(); i++){
            String tabla = tablasSubida.get(i).getTable();
            int lastLog = getLastUploadID(tabla);
            List<UploadableEntity> data = tablasSubida.get(i).getNewerThan(lastLog);
            if (data != null && !data.isEmpty()) {
                long idMaximo = 0L;
                for (UploadableEntity ue : data) {
                    if (ue.getID() > idMaximo) {
                        idMaximo = ue.getID();
                    }
                }
                ultimosID.put(tabla, idMaximo);
                sm.uploadData.add(
                    new DataSincronizacionSubida(tabla, data)
                );
            }
        }
        return ultimosID;
    }

    private void almacenarReservas(Reserva[] reservas) {
        Reservas.INSTANCE.save(reservas);

        List<ConjuntoReserva> conjuntosReservas = new ArrayList<>();
        List<Estacionamiento> estacionamientos = new ArrayList<>();
        List<Persona> personas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.conjunto_reserva != null) {
                conjuntosReservas.add(r.conjunto_reserva);
            }
            if (r.estacionamientos != null) {
                estacionamientos.add(r.estacionamientos);
            }
            if (r.persona != null) {
                personas.add(r.persona);
            }
        }
        ConjuntosReservas.INSTANCE.save(conjuntosReservas);
        Estacionamientos.INSTANCE.save(estacionamientos);
        Personas.INSTANCE.save(personas);
    }

    private void actualizarUltimosID(Map<String, Long> ultimosID) {
        for (String tabla : ultimosID.keySet()) {
            Long idMaximo = ultimosID.get(tabla);
            setLastUploadID(tabla, idMaximo);
        }
    }

    private int getLastUploadID(String tabla) {
        Configuracion p = Configuraciones.INSTANCE.getParametro(Parametro.LAST_UPLOAD_ID_PREFIX + tabla);
        if (p == null) {
            Configuraciones.INSTANCE.save(new Configuracion(Parametro.LAST_UPLOAD_ID_PREFIX + tabla, "0"));
            return 0;
        } else {
            return Integer.parseInt(p.valor);
        }
    }

    private void setLastUploadID(String tabla, Long id) {
        Configuracion p = Configuraciones.INSTANCE.getParametro(Parametro.LAST_UPLOAD_ID_PREFIX + tabla);
        if (p == null) {
            Configuraciones.INSTANCE.save(new Configuracion(Parametro.LAST_UPLOAD_ID_PREFIX + tabla, String.format(Locale.US, "%d", id)));
        } else {
            p.valor = String.format(Locale.US, "%d", id);
            Configuraciones.INSTANCE.save(p);
        }
    }

    public static void cleanTables(){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<String> tableEntities = Arrays.asList(
            "Configuracion",
            "LogInterno",
            "ReservaValidada",
            "BloqueHorario",
            "CanalHorario",
            "ComandoManual",
            "ConjuntoReserva",
            "Controlador",
            "Estacionamiento",
            "LogAcceso",
            "MedioAcceso",
            "ParametroControlador",
            "ParametroPuntoAcceso",
            "Persona",
            "PuntoAcceso",
            "Reserva",
            "RespuestaComandoManual",
            "ResumenTarjetaControlador"
        );
        entityManager.getTransaction().begin();
        for(String table: tableEntities){
            String query = "TRUNCATE TABLE " + table;
            System.out.println(query);
            entityManager.createNativeQuery(query)
                         .executeUpdate();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void main(String[] args) {
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a registration code:");
        try {
            String codeNumber = obj.readLine();
            System.out.print("Registering with code: " + codeNumber);
            cleanTables();
            Synchronizer.registrarDispositivo(codeNumber);
        }
        catch (IOException e){
            System.out.println("Error reading from user");
        }
        System.exit(0);
    }

    public static void registrarDispositivo(String codigoRegistro) {
        Relay.INSTANCE.setup();
        HashMap<String, Object> parametrosRegistro = new HashMap<>();
        parametrosRegistro.put("code", codigoRegistro);
        parametrosRegistro.put("ip", NetworkUtil.getAddress("ip"));
        parametrosRegistro.put("mac", NetworkUtil.getAddress("mac"));
        parametrosRegistro.put("gateway", "192.168.1.1");
        parametrosRegistro.put("serie", CONF.SERIE);
        parametrosRegistro.put("modelo", CONF.MODEL);
        parametrosRegistro.put("version", Double.toString(CONF.VERSION_FULL_ACCESS));
        parametrosRegistro.put("cantidadCanales", CONF.CANTIDAD_CANALES);
        parametrosRegistro.put("capturaHuellas", CONF.CAPTURA_HUELLAS);
        Configuracion parametroApiURL = Configuraciones.INSTANCE.getParametro("API_URL");
        if (parametroApiURL == null) {
            parametroApiURL = new Configuracion();
            parametroApiURL.parametro = "API_URL";
            parametroApiURL.valor = CONF.API_URL;
            Configuraciones.INSTANCE.save(parametroApiURL);
        }
        HttpRequest request = new HttpRequest(parametroApiURL.valor + CONF.URL_REGISTER_DEVICE, parametrosRegistro);
        String respuesta = request.getResponse();
        Log.info("Respuesta de registro: " + respuesta);
        Log.info(request.toString());
        try {
            RespuestaRegistroGK respuestaGK = new Gson().fromJson(respuesta, RespuestaRegistroGK.class);
            if (respuestaGK.estado.equals("OK")) {
                if (respuestaGK.apitoken == null || respuestaGK.base_datos == null || respuestaGK.idDevice == null) {
                    Log.error("Parámetros de respuesta incompletos");
                } else {
                    Configuraciones.INSTANCE.save(new Configuracion(Parametro.API_TOKEN, respuestaGK.apitoken));
                    Configuraciones.INSTANCE.save(new Configuracion(Parametro.DATABASE, respuestaGK.base_datos));
                    Configuraciones.INSTANCE.save(new Configuracion(Parametro.DEVICE_ID, Long.toString(respuestaGK.idDevice)));
                    Log.info("Registro de configuraciones correcto");
                }
            } else {
                Log.error("Estado de respuesta erróneo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.error("Exception en respuesta");
        }
    }
}
