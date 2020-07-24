package cl.dreamit.elevateit.DataModel.Entities.FullAccess;

import cl.dreamit.elevateit.DataModel.DAO.LogsInternos;
import cl.dreamit.elevateit.Utils.Util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.logging.Level;

@Entity
public class LogInterno {
    @Id
    @GeneratedValue
    public int id;
    @Length(max=4096)
    public String mensaje;
    public String level;
    public String fecha;

    public LogInterno(String e){
        super();
        this.mensaje = e;
        this.level = Level.INFO.toString();
        this.fecha = Util.getDateTime(new Date());
    }

    public LogInterno(String e, Level l){
        super();
        this.mensaje = e;
        this.level = l.toString();
        this.fecha = Util.getDateTime(new Date());
    }

    public void save(){
        LogsInternos.INSTANCE.save(this);
    }
}
