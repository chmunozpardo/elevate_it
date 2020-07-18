package cl.dreamit.elevateit.Hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;
import cl.dreamit.elevateit.Synchronizer.SyncControl;

public enum ExitButton {
    INSTANCE;

    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_19, PinPullResistance.PULL_DOWN);

    public void setup() {
        myButton.setShutdownOptions(true);
        myButton.addListener(
            new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    int canal = 0;
                    PuntoAcceso puntoAcceso = PuntosAccesos.INSTANCE.getPuntoAccesoControlador(canal);
                    boolean NC = puntoAcceso.tipoApertura().equals("NC");
                    if(event.getState().isHigh() == NC){
                        SyncControl.INSTANCE.offSync();
                        Relay.INSTANCE.openAll();
                    } else {
                        SyncControl.INSTANCE.onSync();
                        Relay.INSTANCE.closeAll();
                    }
                }
            }
        );
    }
}