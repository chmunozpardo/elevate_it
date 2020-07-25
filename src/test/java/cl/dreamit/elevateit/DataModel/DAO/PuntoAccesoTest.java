package cl.dreamit.elevateit.DataModel.DAO;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;

public class PuntoAccesoTest {

    PuntoAcceso p;

    @BeforeEach
    void init(){
        p = new PuntoAcceso();
        p.id = 1;
        p.nombre = "hola";
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