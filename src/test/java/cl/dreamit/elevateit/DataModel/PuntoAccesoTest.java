package cl.dreamit.elevateit.DataModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;

public class PuntoAccesoTest {

    PuntoAcceso p;

    @BeforeEach
    void init(){
        p = new PuntoAcceso();
        p.id = 1;
        p.nombre = "Punto de prueba";
        p.numeroCanal = 0;
        PuntosAccesos.INSTANCE.save(p);
    }

    @Test
    void ejemplo() {
        PuntoAcceso q = PuntosAccesos.INSTANCE.getPuntoAccesoControlador(0);
        System.out.println(p);
        System.out.println(q.equals(p));
    }
}