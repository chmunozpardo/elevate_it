package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Estacionamiento {
    @Id
    //@GeneratedValue
    public int id;
    public int id_cliente;
    public String numero;
    public int id_area_subarea;
    public String tipo_uso;
    public int id_colaborador;
}
