package cl.dreamit.elevateit.Hardware;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import cl.dreamit.elevateit.Synchronizer.SyncControl;

public enum ExitButton {
    INSTANCE;

    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_19, PinPullResistance.PULL_DOWN);

    public void setup() throws InterruptedException {
        myButton.setShutdownOptions(true);
        myButton.addListener(
            new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    if(event.getState().isHigh()){
                        SyncControl.INSTANCE.offSync();
                        Relay.INSTANCE.openAll();
                    } else if (event.getState().isLow()) {
                        SyncControl.INSTANCE.onSync();
                        Relay.INSTANCE.closeAll();
                    }
                }
            }
        );
    }
}