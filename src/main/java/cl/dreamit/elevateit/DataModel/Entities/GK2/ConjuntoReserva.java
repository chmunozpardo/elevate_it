package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Column;
import javax.persistence.Table;

import cl.dreamit.elevateit.DataModel.DAO.Reservas;

import java.util.List;

@Entity
@Table(
    name = "conjuntoReserva",
    indexes = {
        @Index(
            name = "codigo_reserva",
            columnList = "codigo_reserva"
        ),
        @Index(
            name = "fecha_inicio_index",
            columnList = "fecha_inicio_index"
        ),
        @Index(
            name = "fecha_fin_index",
            columnList = "fecha_fin_index"
        )
    }
)
public class ConjuntoReserva {
    @Id
    @GeneratedValue
    public int id;

    public int id_cliente_creador;
    public int id_cliente_reservado;
    public int id_usuario_creador;
    public int id_area_subarea;
    public String tipo_reserva;
    public String tipo_sujeto;
    public int id_tipo_reserva;
    public int id_tipo_sujeto;
    public String fecha_inicio_index;
    public String fecha_fin_index;
    public String frecuencia;
    public String anfitrion;
    public boolean antipassback;
    public String QR;
    public String comentario;

    public String codigo_reserva;
    public String hash_validacion;

    public List<Reserva> reservasAsociadas(){
        return Reservas.getReservasByIdConjuntoReserva(this.id);
    }

    public List<Reserva> reservasAsociadasFecha(String fecha){
        return Reservas.getReservasByIdConjuntoReserva(this.id, fecha);
    }
}
