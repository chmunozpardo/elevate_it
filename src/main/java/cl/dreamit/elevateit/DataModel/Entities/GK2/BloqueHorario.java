package cl.dreamit.elevateit.DataModel.Entities.GK2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Column;

import com.google.gson.Gson;

@Entity
@Table(
    name = "bloqueHorario",
    indexes = {
        @Index(
            name = "id_canal_horario",
            columnList="id_canal_horario"
        ),
        @Index(
            name = "dia",
            columnList= "dia"
        )
    }
)
public class BloqueHorario {
    @Id
    //@GeneratedValue
    public Integer id;
    public Integer id_canal_horario;
    @Column(length=200)
    public String dia;
    public String inicio;
    public String fin;
    public String deleted_at;

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
