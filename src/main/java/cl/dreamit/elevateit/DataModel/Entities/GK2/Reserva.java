package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;

import cl.dreamit.elevateit.DataModel.Const.Operation;
import cl.dreamit.elevateit.DataModel.DAO.ConjuntosReservas;
import cl.dreamit.elevateit.DataModel.DAO.Estacionamientos;
import cl.dreamit.elevateit.DataModel.DAO.Personas;

@Entity
@Table(
    indexes = {
        @Index(
            name = "id_persona_reservada",
            columnList = "id_persona_reservada"
        ),
        @Index(
            name = "id_conjunto_reserva",
            columnList = "id_conjunto_reserva"
        ),
        @Index(
            name = "fecha_inicio",
            columnList = "fecha_inicio"
        ),
        @Index(
            name = "fecha_fin",
            columnList = "fecha_fin"
        )
    }
)
public class Reserva {
    @Id
    //@GeneratedValue
    public int id;
    public String estado;
    public int id_estado;
    public int id_conjunto_reserva;
    public int id_persona_reservada;
    public int id_proveedor;
    public int id_colaborador;
    public int id_estacionamiento;
    @Column(length=100)
    public String fecha_inicio;
    @Column(length=100)
    public String fecha_fin;
    public String hora_inicio;
    public String hora_fin;
    public String fecha_inicio_index;
    public String fecha_fin_index;
    public String momento_validacion;
    public String momento_termino;
    public int id_validador;
    public String urlFoto;
    public String observacion;
    public String tipo_reserva_gk;

    //public String codigo_reserva; //Este campo ha pasado a ser parte de conjunto_reserva.
    //public String hash_validacion; //Este campo ha pasado a ser parte de conjunto_reserva.

    public String patente;

    //Campos anidados desde la respuesta de la API de GK2. No existe una manera sencilla de enlazar con Android Room.
    //RECORDAR: Estos campos no estarán disponibles si esta Reserva se recupera desde la base de datos!!
    @Transient
    public ConjuntoReserva conjunto_reserva;
    @Transient
    public Estacionamiento estacionamientos;
    @Transient
    public Persona persona;

    public int validar(PuntoAcceso puntoAcceso) {
        if (!puntoAcceso.permiteValidar(this)) {
            return Operation.INVALID_OPEN;
        }
        if (this.estado.equals("Cancelada")) {
            return Operation.INVALID_OPEN;
        }
        if (this.poseeAntipassback() == false) {
            return Operation.VALID_OPEN;
        }
        if (puntoAcceso.antipassbackHabilitado() == false) {
            return Operation.VALID_OPEN;
        }
        if (puntoAcceso.esEntrada() == false && puntoAcceso.esSalida() == false) {
            return Operation.VALID_OPEN;
        } else {
            if (this.estado.equals("Reservada") && puntoAcceso.esEntrada()) {
                return Operation.VALID_OPEN;
            }
            if ((this.estado.equals("Validada") || this.estado.equals("Terminada")) && puntoAcceso.esSalida()) {
                return Operation.VALID_OPEN;
            }
            //Antipassback en un estado no valido.
            return Operation.PASSBACK_REFUSE;
        }
    }

    /**
     * Retorna si esta Reserva está asociada a un ConjuntoReserva con antipassback habilitado.
     *
     * @return
     */
    public boolean poseeAntipassback() {
        ConjuntoReserva conjuntoReserva = getConjuntoReserva();
        return conjuntoReserva == null ? false : conjuntoReserva.antipassback;
    }

    /**
     * Retorna la Persona asociada a esta Reserva desde los datos almacenados en la base de datos interna.
     *
     * @return
     */
    public Persona getPersonaReservada() {
        return Personas.INSTANCE.getById(this.id_persona_reservada);
    }

    public ConjuntoReserva getConjuntoReserva() {
        return ConjuntosReservas.INSTANCE.getByID(this.id_conjunto_reserva);
    }

    public Estacionamiento getEstacionamiento() {
        return Estacionamientos.INSTANCE.getByID(this.id_estacionamiento);
    }
}
