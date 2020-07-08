package cl.dreamit.elevateit.DataModel.Entities.FullAccess;

import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.Utils.Util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "reserva_validada")
public class ReservaValidada extends UploadableEntity {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    public int id;

    public int id_conjunto_reserva;
    public String momentoValidacion;
    public int idControlador;
    public int numeroCanal;

    public ReservaValidada() {
        //Constructor vacio necesario para Android Room
    }

    public ReservaValidada(Reserva r, int idControlador, int numeroCanal) {
        super();
        this.id_conjunto_reserva = r.id_conjunto_reserva;
        this.momentoValidacion = Util.getDateTime(new Date());
        this.idControlador = idControlador;
        this.numeroCanal = numeroCanal;
    }

    @Override
    public String getTable() {
        return "reserva_validada";
    }

    @Override
    public long getID() {
        return this.id;
    }
}
