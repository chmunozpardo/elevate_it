package cl.dreamit.elevateit.Hardware;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.NanoPiGpioProvider;
import com.pi4j.io.gpio.BananaProGpioProvider;
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

public class Buzzer extends PinProvider{

    public static final Pin GPIO_19 = createDigitalPin(RaspiGpioProvider.NAME, 19, "GPIO 19");
    public static final Pin GPIO_20 = createDigitalPin(RaspiGpioProvider.NAME, 20, "GPIO 20");
    private long INTERVAL = 250000;
    private long INTERVAL_MAIN = (long)(1000000000);
}