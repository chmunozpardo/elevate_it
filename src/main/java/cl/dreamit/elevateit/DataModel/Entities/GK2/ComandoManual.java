package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
    name = "comandoManual",
    indexes = {
        @Index(
            name = "id",
            columnList = "id"
        )
    }
)
public class ComandoManual{
    @Id
    //@GeneratedValue
    public int id;
    public int id_controlador;
    public int id_usuario;
    public String comando;

    @Transient
    public Usuario usuario;
}
