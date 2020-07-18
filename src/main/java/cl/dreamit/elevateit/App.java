package cl.dreamit.elevateit;

import java.util.logging.Level;
import java.util.logging.Logger;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.Hardware.ExitButton;
import cl.dreamit.elevateit.Hardware.Relay;
import cl.dreamit.elevateit.Hardware.Wiegand;
import cl.dreamit.elevateit.Synchronizer.Synchronizer;

public final class App{
    public static void main(String[] args) {

        System.out.println("ElevateIT DREAMIT Starting...");
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger(CONF.LOG_NAME).setLevel(Level.INFO);

        Relay.INSTANCE.setup();
        ExitButton.INSTANCE.setup();
        Thread sync = new Thread(new Synchronizer());
        Thread rfid = new Thread(new Wiegand());

        sync.start();
        rfid.start();
    }
}
