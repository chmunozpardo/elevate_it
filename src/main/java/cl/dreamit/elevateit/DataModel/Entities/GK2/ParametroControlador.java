package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParametroControlador {
    @Id
    //@GeneratedValue
    public int id;
    public String parametro;
    public String valor;
    public String valor_override;
    public int modificable;
    public int id_controlador;
}
