package cl.dreamit.elevateit;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.dreamit.elevateit.Synchronizer.Synchronizer;

public final class App{

    @PersistenceContext
    private EntityManager em;

    private App() {
    }

    public EntityManager getEM(){
        return em;
    }

    public static void main(String[] args) throws InterruptedException, IOException{

        System.out.println("Hello World!");

        // Synchronizer.registrarDispositivo("458208");

        new Synchronizer().start();
    }
}
