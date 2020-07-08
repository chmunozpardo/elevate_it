package cl.dreamit.elevateit.DataModel;

import cl.dreamit.elevateit.DataModel.Entities.GK2.CanalHorario;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ComandoManual;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Controlador;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ResumenTarjetaControlador;

public class RespuestaSincronizacion {
    public String estado;
    public String error = "";
    public Long currentTimestamp;            //Timestamp del servidor.

    //public DateTimezoneData localTime; //Fecha y hora que deberia tener este dispositivo segun la configuracion horaria.

    //Data de la respuesta.
    public ResumenTarjetaControlador[] tarjetas;
    public ComandoManual[] comandos;
    public CanalHorario[] canalesHorarios;
    public Reserva[] reservas;
    public Controlador controlador;
    public String qrCode;
}
