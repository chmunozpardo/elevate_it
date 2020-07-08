package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parametroPuntoAcceso")
public class ParametroPuntoAcceso {
    @Id
    //@GeneratedValue
    public int id;
    public String parametro;
    public String valor;
    public String valor_override;
    public int modificable;
    public int id_punto_acceso;
}
