package cl.dreamit.elevateit.Hardware;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import cl.dreamit.elevateit.Configuration.CONF;
@SuppressWarnings("serial")
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
        deviceI2C.write(0x02, (byte)0x00);
        deviceI2C.write(0x03, (byte)0x00);
        deviceI2C.write(0x06, (byte)0x00);
        deviceI2C.write(0x07, (byte)0x00);
    }

    public void openRelay(int puntoAcceso) {
        new Thread(() -> {
            try{
                int addr = 0;
                if(puntoAcceso < 8){
                    addr = 0x02;
                } else if(puntoAcceso >= 8){
                    addr = 0x03;
                }
                int read_data = deviceI2C.read(addr);
                read_data |= (0x01 << (puntoAcceso % 8));
                deviceI2C.write(addr, (byte)read_data);
                Thread.sleep(CONF.DEFAULT_OPEN_TIME);
                read_data = deviceI2C.read(addr);
                read_data &= ~(0x01 << (puntoAcceso % 8));
                deviceI2C.write(addr, (byte)read_data);
            } catch(Exception ignored) {}
        }).start();
    }

    public void openRelay(List<Integer> puntosAcceso) {
        new Thread(() -> {
            try{
                int writeData = 0;
                for(Integer puntoAcceso : puntosAcceso){
                    writeData |= (0x01 << puntoAcceso);
                }
                int lowerData = writeData & (0xFF);
                int upperData = (writeData >> 8) & (0xFF);
                int readLowerData = deviceI2C.read(0x02);
                int readUpperData = deviceI2C.read(0x03);
                readLowerData |= lowerData;
                readUpperData |= upperData;
                deviceI2C.write(0x02, (byte)readLowerData);
                deviceI2C.write(0x03, (byte)readUpperData);
                Thread.sleep(CONF.DEFAULT_OPEN_TIME);
                readLowerData = deviceI2C.read(0x02);
                readLowerData &= ~lowerData;
                deviceI2C.write(0x02, (byte)readLowerData);
                readUpperData = deviceI2C.read(0x03);
                readUpperData &= ~upperData;
                deviceI2C.write(0x03, (byte)readUpperData);
            } catch(Exception ignored) {}
        }).start();
    }
}
