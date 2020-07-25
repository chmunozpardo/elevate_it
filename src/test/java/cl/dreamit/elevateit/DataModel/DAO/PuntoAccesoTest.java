package cl.dreamit.elevateit.DataModel.DAO;

import org.junit.jupiter.api.Test;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PuntoAccesoTest {

    @Test
    void ejemplo() {
        PuntoAcceso p = new PuntoAcceso();
        p.id = 1;
        p.nombre = "hola";
        List<PuntoAcceso> listP = new Arrays.asList(p);
        PuntosAccesos.INSTANCE.save(p);
        System.out.println("Test");
    }
}