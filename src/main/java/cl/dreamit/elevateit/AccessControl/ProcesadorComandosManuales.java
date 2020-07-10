package cl.dreamit.elevateit.AccessControl;

import java.util.Date;

import cl.dreamit.elevateit.DataModel.Const.Operation;
import cl.dreamit.elevateit.DataModel.Const.ReadSource;
import cl.dreamit.elevateit.DataModel.DAO.Configuraciones;
import cl.dreamit.elevateit.DataModel.DAO.Controladores;
import cl.dreamit.elevateit.DataModel.DAO.LogsAcceso;
import cl.dreamit.elevateit.DataModel.DAO.RespuestasComandos;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.Configuracion;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ComandoManual;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Controlador;
import cl.dreamit.elevateit.DataModel.Entities.GK2.LogAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;
import cl.dreamit.elevateit.DataModel.Entities.GK2.RespuestaComandoManual;
import cl.dreamit.elevateit.Utils.Log;
import cl.dreamit.elevateit.Utils.Util;

public class ProcesadorComandosManuales {

    public final static String COMANDO_OPEN = "OUTPUT:OPEN:";

    public static void procesarComandos(ComandoManual[] comandos) {
        for (ComandoManual comandoManual : comandos) {
            Log.error("Procesando Comando Manual: " + comandoManual);
            if (comandoManual.comando.startsWith(COMANDO_OPEN)) {
                procesar_comando_open(comandoManual);
            }
        }
    }

    private static void procesar_comando_open(ComandoManual comandoManual) {
        Log.error("Solicitud de apertura desde sistema GK");
        try {
            //Almacenamos el Log. Para ello necesitamos primero el Controlador.
            Configuracion idControlador = Configuraciones.getParametro("idDevice");
            Controlador controlador = Controladores.getByID(Integer.parseInt(idControlador.valor));
            if (controlador == null) {
                return;
            }
            int numeroCanalSolicitado = Integer.parseInt(comandoManual.comando.substring(COMANDO_OPEN.length()));
            PuntoAcceso puntoAcceso = controlador.obtenerPuntoAcceso(numeroCanalSolicitado);
            if (puntoAcceso == null) {
                return;
            }
            LogAcceso log = new LogAcceso();
            log.id_controlador = controlador.id;
            log.id_punto_acceso = puntoAcceso.id;
            log.id_cliente = controlador.id_propietario;
            log.nombre_controlador = controlador.nombre;
            log.nombre_punto_acceso = puntoAcceso.nombre;
            log.tipoMedioAcceso = 0;
            log.id_operacion_lectura = Operation.VALID_OPEN;
            log.fecha_registro = Util.getDateTime(new Date());
            log.id_origen_lectura = ReadSource.SOURCE_SOFTWARE;
            log.codigo_medio_acceso = String.format("%d", comandoManual.id_usuario);
            LogsAcceso.INSTANCE.save(log);
            RespuestasComandos.INSTANCE.save(
                new RespuestaComandoManual(comandoManual.id, "OK")
            );
            // TODO activar relé
            cl.dreamit.elevateit.Hardware.Relay rele = new cl.dreamit.elevateit.Hardware.Relay(0x20);
            rele.openRelay(numeroCanalSolicitado);
        } catch (Exception ex) {}
    }
}
