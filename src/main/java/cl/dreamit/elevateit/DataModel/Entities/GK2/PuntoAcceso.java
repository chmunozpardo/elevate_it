package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.Const.Antipassback;
import cl.dreamit.elevateit.DataModel.Const.Operation;
import cl.dreamit.elevateit.DataModel.DAO.ParametrosPuntosAccesos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.Gson;

@Entity
@Table(name = "puntoAcceso")
public class PuntoAcceso {
    @Id
    //@GeneratedValue
    public int id;
    public String nombre;
    public int numeroCanal;
    public int estado;
    public String bloqueo;
    public int id_controlador;
    public int compartido;
    public int id_area_subarea;

    /* Estructura desde la API */
    @Transient
    public ParametroPuntoAcceso[] parametros_punto_acceso;

    public boolean esEntrada() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "sentido");
        if (p == null) {
            return false;
        }
        String sentido = p.valor_override != null ? p.valor_override : p.valor;
        return sentido.equals("Entrada");
    }

    public double temperaturaMinima() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id,"minTemp");
        double outTemp;

        if(p!= null){
            String readTempValor = p.valor_override != null ? p.valor_override : p.valor;
            outTemp = Double.parseDouble(readTempValor);
        } else {
            outTemp = 35.0;
        }
        return outTemp;
    }

    public double temperaturaMaxima() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id,"maxTemp");
        double outTemp;
        if(p!= null){
            String readTempValor = p.valor_override != null ? p.valor_override : p.valor;
            outTemp = Double.parseDouble(readTempValor);
        } else {
            outTemp = 37.0;
        }
        return outTemp;
    }

    public boolean esSalida() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "sentido");
        if (p == null) {
            return false;
        }
        String sentido = p.valor_override != null ? p.valor_override : p.valor;
        return sentido.equals("Salida");
    }

    public boolean antipassbackHabilitado() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "antiPassback");
        if (p == null) {
            return false;
        }
        String sentido = p.valor_override != null ? p.valor_override : p.valor;
        return sentido.equals("1");
    }

    /**
     * Retorna el tiempo de apertura para este canal; Si algo sale mal retorna el valor de configuración por defecto 5 seg.
     *
     * @return tiempo
     */
    public int tiempoApertura() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "tiempoApertura");
        if (p == null) {
            return CONF.DEFAULT_OPEN_TIME;
        }
        String sentido = p.valor_override != null ? p.valor_override : p.valor;
        try {
            return Integer.parseInt(sentido);
        } catch (Exception ex) {
            return CONF.DEFAULT_OPEN_TIME;
        }
    }

    public int validar(ResumenTarjetaControlador rtc) {
        int permisoNecesario = 1 << numeroCanal;
        if ((permisoNecesario & rtc.permisos) == 0) {
            //LOG.info(true,"Rechazo de Apertura. El Registro no cuenta con los privilegios suficientes para este Punto de Acceso");
            return Operation.PERMISSION_REFUSE;
        }
        //Validación Antipassback.
        if ((esEntrada() || esSalida()) && antipassbackHabilitado()) {
            if (rtc.antipassbackStatus == Antipassback.SOLO_ENTRADA && !esEntrada()) {
                //La Persona solo puede Entrar, y este punto no es de entrada.
                //LOG.info(true, "Rechazo Antipassback. Este registro solo permite Ingresar actualmente");
                return Operation.PASSBACK_REFUSE;
            }
            if (rtc.antipassbackStatus == Antipassback.SOLO_SALIDA && !esSalida()) {
                //Lo mismo pero en salida.
                //LOG.info(true, "Rechazo Antipassback. Este registro solo permite Salir actualmente");
                return Operation.PASSBACK_REFUSE;
            }
        }

        //Validaciones listas. no hay motivo para rechazar su validación.
        return Operation.VALID_OPEN;
    }

    public boolean permiteValidar(Reserva reserva) {
        ConjuntoReserva conjunto_reserva = reserva.getConjuntoReserva();
        //Punto de espacio común. puede validar cualquier reserva.
        if (this.esEspacioComun()) {
            return true;
        } else if (this.esAccesoVehicular() && conjunto_reserva != null && "Vehicular".equals(conjunto_reserva.tipo_reserva)) {
             //Este punto es acceso vehicular, y la reserva también.
            return true;
        } else if (this.validaReservas()) {
            //Ultima opción. Este punto si valida reservas
            Estacionamiento estacionamientoReserva = reserva.getEstacionamiento();
            //Y el area de destino de la reserva es la misma que la de de este punto de acceso
            if (estacionamientoReserva != null && this.id_area_subarea == estacionamientoReserva.id_area_subarea) {
                //Y el estacionamiento de la reserva está en la misma area que este punto de acceso.
                return true;
            }
            else return conjunto_reserva != null && this.id_area_subarea == conjunto_reserva.id_area_subarea;
        }
        return false;
    }

    private boolean validaReservas() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "validacionReservas");
        return p != null && "1".equals(p.valor_override);
    }

    private boolean esAccesoVehicular() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "accesoVehicular");
        return p != null && "1".equals(p.valor_override);
    }

    private boolean esEspacioComun() {
        ParametroPuntoAcceso p = ParametrosPuntosAccesos.getParametroPuntoAcceso(this.id, "espacioComun");
        return p != null && "1".equals(p.valor_override); }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
