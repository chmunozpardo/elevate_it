package cl.dreamit.elevateit.Hardware;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.NanoPiGpioProvider;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.BananaProGpioProvider;
import com.pi4j.io.gpio.BananaProPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.io.gpio.NanoPiPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinProvider;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.PlatformAlreadyAssignedException;

@SuppressWarnings("serial")
public class Buzzer extends PinProvider{

    static {
        System.loadLibrary("elevateit");
    }

    public static Map<String, Double> notesTable = new HashMap<String, Double>(){{
        put("C4" , 261.63);
        put("C4#", 277.18);
        put("D4" , 293.66);
        put("D4#", 311.13);
        put("E4" , 329.63);
        put("F4" , 349.23);
        put("F4#", 369.99);
        put("G4" , 392.00);
        put("G4#", 415.30);
        put("A4" , 440.00);
        put("A4#", 466.16);
        put("B4" , 493.88);

        put("C5" , 523.25);
        put("C5#", 554.37);
        put("D5" , 587.33);
        put("D5#", 622.25);
        put("E5" , 659.25);
        put("F5" , 698.46);
        put("F5#", 739.99);
        put("G5" , 783.99);
        put("G5#", 830.61);
        put("A5" , 880.00);
        put("A5#", 932.33);
        put("B5" , 987.77);
    }};

    public static native void buzz(double frequency, int duration);

    public static void main(String[] args) throws InterruptedException, PlatformAlreadyAssignedException {
        buzz(notesTable.get("C5"), 500);
        buzz(notesTable.get("E5"), 500);
        buzz(notesTable.get("G5"), 500);
        buzz(notesTable.get("E5"), 500);
        buzz(notesTable.get("C5"), 500);
    }
}