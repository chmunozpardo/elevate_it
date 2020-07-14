package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cl.dreamit.elevateit.DataModel.DAO.Reservas;

import java.util.List;

@Entity
public class Persona {
    @Id
    //@GeneratedValue
    public int id;
    public int id_tipo_credencial;
    public String nombre;
    public String apellido;
    public String credencial;
    public String email;
    public String telefono;
    public int id_pais_origen;
    public int id_pais_pasaporte;
    public boolean habilitado;

    public List<Reserva> obtenerReservasValidas(String date) {
        //LOG.error("SELECT * FROM reserva WHERE fecha_inicio_index <= '%s' AND fecha_fin_index >= '%s' AND id_persona_reservada = %d", date, date, this.id);
        return Reservas.INSTANCE.getReservasPersonaFecha(this.id, date);
    }
}
