package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.UploadableEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@Table(
    indexes = {
        @Index(
            name = "id",
            columnList = "id"
        )
    }
)
public class LogAcceso extends UploadableEntity {
    @Id
    @GeneratedValue
    public int id;
    public int id_controlador;
    public int id_punto_acceso;
    public Integer id_cliente;
    public Integer id_cliente_real;
    public Integer id_medio_acceso;
    public String nombre_controlador;
    public String nombre_punto_acceso;
    public int id_origen_lectura;
    public int tipoMedioAcceso;
    public String codigo_medio_acceso;
    public String nombre_portador_medio_acceso;
    public String credencial;
    public int id_operacion_lectura;
    public String fecha_registro;

    @Transient
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LogAcceso() {
    }

    @Override
    public String getTable() {
        return "registros_acceso";
    }

    @Override
    public long getID() {
        return this.id;
    }
}
