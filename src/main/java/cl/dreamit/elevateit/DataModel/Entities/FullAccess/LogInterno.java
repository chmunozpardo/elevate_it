package cl.dreamit.elevateit.DataModel.Entities.FullAccess;

import cl.dreamit.elevateit.DataModel.DAO.LogsInternos;
import cl.dreamit.elevateit.Utils.Util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Date;

@Entity
public class LogInterno {
    @Id
    @GeneratedValue
    public int id;
    @Column(length = 4096)
    public String mensaje;
    public String fecha;

    // TODO posible null id?
    public LogInterno(String e){
        super();
        this.mensaje = e;
        this.fecha = Util.getDateTime(new Date());
    }

    public void save(){
        LogsInternos.save(this);
    }
}
