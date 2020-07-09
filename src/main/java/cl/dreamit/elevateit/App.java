package cl.dreamit.elevateit;

import java.io.IOException;

import cl.dreamit.elevateit.Synchronizer.Synchronizer;

public final class App{

    private App() {
    }

    public static void main(String[] args) throws InterruptedException, IOException{

        System.out.println("Hello World!");

        // Synchronizer.registrarDispositivo("458208");

        new Synchronizer().start();
    }
}
