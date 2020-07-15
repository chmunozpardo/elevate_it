package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.Const.Antipassback;
import cl.dreamit.elevateit.DataModel.Const.Operation;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
@Table(
    indexes = {
        @Index(
            name = "card_type",
            columnList = "card_type"
        ),
        @Index(
            name = "card_code_1",
            columnList = "card_code_1,card_code_2"
        )
    }
)
public class ResumenTarjetaControlador {
    @Id
    //@GeneratedValue
    public int id;
    public int id_controlador;
    public int id_colaborador;
    public int id_medio_acceso;
    public int id_cliente;
    public int permisos;
    public int card_type;
    public long card_code_1;
    public long card_code_2;
    public int id_canal_horario;
    public String nombre;
    public String apellido;
    public String credencial;
    public int antipassbackStatus;

    public int validar(PuntoAcceso puntoValidacion) {
        int permisoNecesario = 1 << puntoValidacion.numeroCanal;
        if ((permisoNecesario & this.permisos) == 0) {
            //LOG.info(true,"Rechazo de Apertura. El Registro no cuenta con los privilegios suficientes para este Punto de Acceso");
            return Operation.PERMISSION_REFUSE;
        }
        //Canal Horario activo?
        if (this.id_canal_horario != 0) {
            //EL Usuario tiene un Canal Horario. debemos validarlo.
            //LOG.info(true,"Validando Canal horario ID: " + id_canal_horario);
            //CanalHorario canalHorario = Database.canalesHorarios().getById(id_canal_horario);
            CanalHorario canalHorario = new CanalHorario();

            if (canalHorario != null && !canalHorario.esValido()) {
                //LOG.info(true,"El canal Horario no está en un horario válido");
                return Operation.TIME_REFUSE;
            }
        }

        //Validacion Antipassback.
        if ((puntoValidacion.esEntrada() || puntoValidacion.esSalida()) && puntoValidacion.antipassbackHabilitado()) {
            if (antipassbackStatus == Antipassback.SOLO_ENTRADA && !puntoValidacion.esEntrada()) {
                //La Persona solo puede Entrar, y este punto no es de entrada.
                //LOG.info(true,"Rechazo Antipassback. Este registro solo permite Ingresar actualmente");
                return Operation.PASSBACK_REFUSE;
            }
            if (antipassbackStatus == Antipassback.SOLO_SALIDA && !puntoValidacion.esSalida()) {
                //Lo mismo pero en salida.
                //L        OG.info(true, "Rechazo Antipassback. Este registro solo permite Salir actualmente");
                return Operation.PASSBACK_REFUSE;
            }
        }
        //Validaciones listas. no hay motivo para rechazar su validacion.
        return Operation.VALID_OPEN;
        }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
