package cl.dreamit.elevateit.DataModel.Entities.GK2;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.UploadableEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Column;
import javax.persistence.Table;

@Entity
@Table(
    name = "respuestaComandoManual",
    indexes = {
        @Index(
            name = "id",
            columnList = "id"
        )
    }
)
public class RespuestaComandoManual extends UploadableEntity
    {
    @Id
    //@GeneratedValue
    @Column(nullable = false)
    public int id;

    @Column(name = "id_comandoManual")
    public int id_comandoManual;

    @Column(name = "respuesta")
    public String respuesta;

    @Override
    public String getTable() {
        return "respuestaComandoManual";
    }

    @Override
    public long getID() {
        return this.id;
    }

    public RespuestaComandoManual(){
    }

    public RespuestaComandoManual(int requestID, String response) {
        this.id_comandoManual = requestID;
        this.respuesta = response;
    }
}
