package cl.dreamit.elevateit;

import java.io.IOException;
import java.util.logging.Level;

import cl.dreamit.elevateit.Hardware.Wiegand;
import cl.dreamit.elevateit.Synchronizer.Synchronizer;

public final class App{

    private App() {
    }

    public static void main(String[] args) throws InterruptedException, IOException{

        System.out.println("Hello World!");

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        // Synchronizer.registrarDispositivo("294969");

        Thread sync = new Thread(new Synchronizer());
        sync.start();

        Wiegand test = new Wiegand();
        while(true){
            test.readCard();
            test.print();
            test.searchCard();
        }
    }
}
