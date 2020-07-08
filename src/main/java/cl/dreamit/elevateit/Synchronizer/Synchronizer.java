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
import cl.dreamit.elevateit.DataModel.DAO.ParametrosControladores;
import cl.dreamit.elevateit.DataModel.DAO.ParametrosPuntosAccesos;
import cl.dreamit.elevateit.DataModel.DAO.Personas;
import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;
import cl.dreamit.elevateit.DataModel.DAO.Reservas;
import cl.dreamit.elevateit.DataModel.DAO.ReservasValidadas;
import cl.dreamit.elevateit.DataModel.DAO.RespuestasComandos;
import cl.dreamit.elevateit.DataModel.DAO.TarjetasAcceso;
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
import cl.dreamit.elevateit.Utils.HttpRequest;
import cl.dreamit.elevateit.Utils.Log;
import cl.dreamit.elevateit.Utils.NetworkUtil;
import cl.dreamit.elevateit.Utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;

public class Synchronizer extends Thread {
    private String targetDatabase = null;
    private String apiToken = null;
    private String apiURL = null;
    private Long lastSyncTimestamp;

    public Synchronizer(){
        apiToken = Configuraciones.getParametro("apitoken").valor;
        targetDatabase = Configuraciones.getParametro("base_datos").valor;
        apiURL = Configuraciones.getParametro("API_URL").valor;
    }

    public void run() {
        Configuracion timeStamp = Configuraciones.getParametro("lastSyncTimestamp");
        if(timeStamp == null){
            timeStamp = new Configuracion();
            timeStamp.parametro = "lastSyncTimestamp";
            timeStamp.valor = "0";
            Configuraciones.save(timeStamp);
        }
        lastSyncTimestamp = Long.parseLong(timeStamp.valor);
        while(true){
            try{
                Thread.sleep(CONF.TIME_SYNC);
            } catch (InterruptedException ex){}
            SyncMessage message = new SyncMessage(lastSyncTimestamp);

            //Verifica si hay datos que enviar a la nube.
            Map<String, Long> ultimosID = cargarDatos(message);

            Map<String, Object> postData = new HashMap<>();
            postData.put("api_token", apiToken);
            postData.put("nombreInstancia", targetDatabase);
            postData.put("syncData", message);
            postData.put("fechaValidez", Util.getDateTime(new Date()));

            HttpRequest request = new HttpRequest(apiURL + CONF.URL_SYNC_DATA, postData);
            String respuesta = request.getResponse();
            //Log.error("Respuesta de Sync: " + respuesta);

            try {
                RespuestaSincronizacion syncResponse = new Gson().fromJson(respuesta, RespuestaSincronizacion.class);
                if (syncResponse.estado.equals("OK")) {
                    //bien. carga el nuevo timestamp de ultima sincronización.
                    lastSyncTimestamp = syncResponse.currentTimestamp;
                    //actualizarFechaHora(syncResponse.localTime); //Descartado. Solo Apps a nivel de sistema pueden hacer esto.
                    //Almacena todos los medios de acceso obtenidos.
                    TarjetasAcceso.save(syncResponse.tarjetas);
                    //Los canales horarios, Reservas y Controladores contienen estructuras compuestas.
                    //TODO podría ser posible almacenar todo y filo; con un algoritmo de limpieza no debería ser problema el almacenamiento
                    almacenarCanalesHorarios(syncResponse.canalesHorarios);
                    almacenarReservas(syncResponse.reservas);
                    almacenarControlador(syncResponse.controlador);
                    //Los comandosManuales deben ser procesados. Pero en un hilo independiente.
                    ProcesadorComandosManuales.procesarComandos(syncResponse.comandos);
                    ComandosManuales.save(syncResponse.comandos); //Los Comandos manuales no tiene sentido almacenarlos; Solo procesarlos.
                    //Cargamos los ultimos ID subidos en la tabla de configuracion.
                    actualizarUltimosID(ultimosID);
                } else if (syncResponse.estado.equals("ERROR")) {
                    //Hubo error. Verifica el tipo de error.
                    switch (syncResponse.error) {
                        case "":
                            Log.error("Error no especificado. Revisar plataforma");
                            break;
                        case SYNC_ERROR.INVALID_API_TOKEN:
                            Log.error("El API Token no es válido.");
                            break;
                    }
                }
            } catch (Exception ex) {
                Log.error("Error decodificando JSON: " + ex.getMessage());
                //Log.error("JSON Input: " + respuesta);
                Log.error("URL: " + request.toString());
                ex.printStackTrace();
            }
        }
    }

    public static void registrarDispositivo(String codigoRegistro) {
        HashMap<String, Object> parametrosRegistro = new HashMap<>();
        parametrosRegistro.put("code", codigoRegistro);
        parametrosRegistro.put("ip", NetworkUtil.getAddress("ip"));
        parametrosRegistro.put("mac", NetworkUtil.getAddress("mac"));
        parametrosRegistro.put("gateway", "192.168.1.1");
        parametrosRegistro.put("serie", "1234lander");
        parametrosRegistro.put("modelo", "elevateIT");
        parametrosRegistro.put("version", Double.toString(CONF.VERSION_FULL_ACCESS));
        parametrosRegistro.put("cantidadCanales", CONF.CANTIDAD_CANALES);
        parametrosRegistro.put("capturaHuellas", CONF.CAPTURA_HUELLAS);
        Configuracion parametroApiURL = Configuraciones.getParametro("API_URL");
        if (parametroApiURL == null) {
            parametroApiURL = new Configuracion();
            parametroApiURL.parametro = "API_URL";
            parametroApiURL.valor = CONF.API_URL;
            Configuraciones.save(parametroApiURL);
        }
        HttpRequest request = new HttpRequest(parametroApiURL.valor + CONF.URL_REGISTER_DEVICE, parametrosRegistro);
        String respuesta = request.getResponse();
        Log.error("Respuesta de registro: " + respuesta);
        Log.error(request.toString());
        try {
            RespuestaRegistroGK respuestaGK = new Gson().fromJson(respuesta, RespuestaRegistroGK.class);
            if (respuestaGK.estado.equals("OK")) {
                //Están los parámetros necesarios para el registro?
                if (respuestaGK.apitoken == null || respuestaGK.base_datos == null || respuestaGK.idDevice == null) {
                    //Lanza el dialog error.
                    Log.error("Parámetros de respuesta incompletos");
                } else {
                    //Registro OK. Copia los datos de registro en nuestra DB local.
                    Configuraciones.save(new Configuracion(Parametro.API_TOKEN, respuestaGK.apitoken));
                    Configuraciones.save(new Configuracion(Parametro.DATABASE, respuestaGK.base_datos));
                    Configuraciones.save(new Configuracion(Parametro.DEVICE_ID, Long.toString(respuestaGK.idDevice)));
                    Log.error("Registro de configuraciones correcto");
                    //Lanza el dialog successfull.
                }
            } else {
                //Lanza el dialog error.
                Log.error("Estado de respuesta erróneo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.error("Exception en respuesta");
            //Lanza el dialog error.
        }
    }

    private void almacenarCanalesHorarios(CanalHorario[] canalesHorarios) {
        if (canalesHorarios == null) {
            return;
        }
        CanalesHorarios.save(canalesHorarios);
        for (CanalHorario c : canalesHorarios) {
            if (c.bloques_horario_trashed != null) {
                BloquesCanalesHorarios.save(c.bloques_horario_trashed);
            }
        }
    }

    private void almacenarControlador(Controlador controlador) {
        if (controlador == null) {
            return;
        }
        //Primero se almacenan los controladores.
        Controladores.save(controlador);
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
        PuntosAccesos.save(puntosAccesos);
        ParametrosControladores.save(parametrosControlador);
        ParametrosPuntosAccesos.save(parametrosPuntosAccesos);
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

        String tabla = LogsAcceso.getTable();
        int lastLog = getLastUploadID(tabla);
        List<UploadableEntity> data =
            new ArrayList<UploadableEntity>(LogsAcceso.getNewerThan(lastLog));
        if (data != null && !data.isEmpty()) {
            //LOG.error("Cargando datos de tabla: %s: %s", tabla, data.toString());
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

        tabla = RespuestasComandos.getTable();
        lastLog = getLastUploadID(tabla);
        data =
            new ArrayList<UploadableEntity>(RespuestasComandos.getNewerThan(lastLog));
        if (data != null && !data.isEmpty()) {
            //LOG.error("Cargando datos de tabla: %s: %s", tabla, data.toString());
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

        tabla = ReservasValidadas.getTable();
        lastLog = getLastUploadID(tabla);
        data =
            new ArrayList<UploadableEntity>(ReservasValidadas.getNewerThan(lastLog));
        if (data != null && !data.isEmpty()) {
            //LOG.error("Cargando datos de tabla: %s: %s", tabla, data.toString());
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

        return ultimosID;
    }

    private void almacenarReservas(Reserva[] reservas) {
        //Primero almacenamos las reservas propiamente tal.
        Reservas.save(reservas);

        //Luego almacenamos los elementos anidados.
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
        ConjuntosReservas.save(conjuntosReservas);
        Estacionamientos.save(estacionamientos);
        Personas.save(personas);
    }

    private void actualizarUltimosID(Map<String, Long> ultimosID) {
        for (String tabla : ultimosID.keySet()) {
            Long idMaximo = ultimosID.get(tabla);
            setLastUploadID(tabla, idMaximo);
        }
    }

    private int getLastUploadID(String tabla) {
        Configuracion p = Configuraciones.getParametro(Parametro.LAST_UPLOAD_ID_PREFIX + tabla);
        if (p == null) {
            Configuraciones.save(new Configuracion(Parametro.LAST_UPLOAD_ID_PREFIX + tabla, "0"));
            return 0;
        } else {
            return Integer.parseInt(p.valor);
        }
    }

    private void setLastUploadID(String tabla, Long id) {
        //Verifica si existe dicho valor, se antepone el string "lastUploadID_" al nombre de la tabla.
        Configuracion p = Configuraciones.getParametro(Parametro.LAST_UPLOAD_ID_PREFIX + tabla);
        if (p == null) {
            Configuraciones.save(new Configuracion(Parametro.LAST_UPLOAD_ID_PREFIX + tabla, String.format(Locale.US, "%d", id)));
        } else {
            // Parámetro ya existe. Actualízalo.
            p.valor = String.format(Locale.US, "%d", id);
            Configuraciones.save(p);
        }
    }
}
