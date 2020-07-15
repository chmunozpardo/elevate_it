package cl.dreamit.elevateit;

import java.io.IOException;
import java.util.logging.Level;

import cl.dreamit.elevateit.Hardware.Wiegand;
import cl.dreamit.elevateit.Synchronizer.Synchronizer;

public final class App{

    private App() {
    }

    public static void main(String[] args) throws InterruptedException, IOException{

        System.out.println("ElevateIT@DREAMIT Starting...");

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Thread sync = new Thread(new Synchronizer());
        Thread rfid = new Thread(new Wiegand());
        sync.start();
        rfid.start();
    }
}
