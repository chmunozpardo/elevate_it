package cl.dreamit.elevateit.DataModel.Entities.FullAccess;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Column;
import javax.persistence.Table;

@Entity
@Table(
    indexes = {
        @Index(
            name = "parametro",
            columnList="parametro",
            unique = true
        )
    }
)
public class Configuracion {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    public int id;

    @Column(name = "parametro")
    public String parametro;

    @Column(name = "valor")
    public String valor;

    public Configuracion(){
    }

    public Configuracion(String param, String value){
        this.parametro = param;
        this.valor = value;
    }

    @Override
    public String toString(){
        return "ParametroConfiguracion: " + parametro + "=" + valor;
    }
}
