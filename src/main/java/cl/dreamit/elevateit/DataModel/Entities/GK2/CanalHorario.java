package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.DAO.BloquesCanalesHorarios;
import cl.dreamit.elevateit.Utils.Util;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class CanalHorario {
    @Transient
    private static int DATA_BYTES = 84;

    @Id
    //@GeneratedValue
    public int id;
    public String nombre;
    public Integer id_propietario;
    public String nombre_tabla_propietario;
    public String deleted_at;

    //Bloques Horarios recuperados desde la API.
    @Transient
    public BloqueHorario[] bloques_horario_trashed;

    @Transient
    public final static String[] diasSemana = new String[] {
        "Lunes",
        "Martes",
        "Miercoles",
        "Jueves",
        "Viernes",
        "Sabado",
        "Domingo"
    };

    /**
     * Indica si un CanalHorario es valido para la hora y fecha actual.
     *
     * @return
     */
    public boolean esValido() {
        Date ahora = new Date();
        String nombreDia = diasSemana[Util.getDiaSemana(ahora) - 1];
        String horaActual = new SimpleDateFormat("H:m").format(ahora);
        List<BloqueHorario> bloquesValidos = BloquesCanalesHorarios.INSTANCE.getByDiaHora(this.id, nombreDia, horaActual);
        return !bloquesValidos.isEmpty();
    }
}
