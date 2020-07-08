package cl.dreamit.elevateit.Hardware;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import cl.dreamit.elevateit.Configuration.CONF;

public class Relay {

    private I2CBus busI2C;
    private I2CDevice deviceI2C;
    private int responseI2C;

    private Map<Integer, String> commandsPCA9555 = new HashMap<Integer, String>() {{
        put(0, "Input port 0");
        put(1, "Input port 1");
        put(2, "Output port 0");
        put(3, "Output port 1");
        put(4, "Polarity inversion port 0");
        put(5, "Polarity inversion port 1");
        put(6, "Configuration port 0");
        put(7, "Configuration port 1");
    }};

    public Relay(int controllerAddress) throws IOException, UnsupportedBusNumberException {
        busI2C = I2CFactory.getInstance(I2CBus.BUS_0);
        deviceI2C = busI2C.getDevice(controllerAddress);
        responseI2C = deviceI2C.read((byte) 0x06);
        System.out.println("Valor de configuración puerto 0 = " + responseI2C);
        responseI2C = deviceI2C.read((byte) 0x07);
        System.out.println("Valor de configuración puerto 1 = " + responseI2C);
        for(Map.Entry<Integer, String> entry : commandsPCA9555.entrySet()){
            System.out.println(String.format("Comando %d = %s", entry.getKey(), entry.getValue()));
        }
    }

    public void openRelay(int puntoAcceso) {
        new Thread(() -> {
            try{
                deviceI2C.write(0x06, (byte)0x00);
                deviceI2C.write(0x02, (byte)(0x01 << puntoAcceso));
                Thread.sleep(CONF.DEFAULT_OPEN_TIME);
                deviceI2C.write(0x02, (byte)0x00);
            } catch(Exception ignored) {}
        }).start();
    }
}