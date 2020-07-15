package cl.dreamit.elevateit.Hardware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// import java.util.Map;
// import java.util.HashMap;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import cl.dreamit.elevateit.Configuration.CONF;
//@SuppressWarnings("serial")
public enum Relay {
    INSTANCE;

    private I2CBus busI2C;
    private List<I2CDevice> devicesI2C = new ArrayList<I2CDevice>();;
    private List<Integer> accessPoints = new ArrayList<Integer>();

    // private Map<Integer, String> commandsPCA9555 = new HashMap<Integer, String>() {{
    //     put(0, "Input port 0");
    //     put(1, "Input port 1");
    //     put(2, "Output port 0");
    //     put(3, "Output port 1");
    //     put(4, "Polarity inversion port 0");
    //     put(5, "Polarity inversion port 1");
    //     put(6, "Configuration port 0");
    //     put(7, "Configuration port 1");
    // }};

    private Relay() {
        try{
            busI2C = I2CFactory.getInstance(I2CBus.BUS_0);
            for(Integer address : CONF.I2C_ADDRESSES){
                I2CDevice i2cdevice = busI2C.getDevice(address);
                devicesI2C.add(i2cdevice);
                for(int i = 0; i < 16; i++){
                    accessPoints.add(0);
                }
                i2cdevice.write(0x02, (byte)0x00);
                i2cdevice.write(0x03, (byte)0x00);
                i2cdevice.write(0x06, (byte)0x00);
                i2cdevice.write(0x07, (byte)0x00);
            }
        } catch(Exception ex){}
    }

    public void openRelay(int puntoAcceso) {
        new Thread(() -> {
            try{
                List<Integer> puntosAcceso = Arrays.asList(puntoAcceso);
                setRelays(puntosAcceso);
                Thread.sleep(CONF.DEFAULT_OPEN_TIME);
                releaseRelays(puntosAcceso);
            } catch(Exception ignored) {}
        }).start();
    }

    public void openRelay(List<Integer> puntosAcceso) {
        new Thread(() -> {
            try{
                setRelays(puntosAcceso);
                Thread.sleep(CONF.DEFAULT_OPEN_TIME);
                releaseRelays(puntosAcceso);
            } catch(Exception ignored) {}
        }).start();
    }

    private synchronized void setRelays(List<Integer> puntosAcceso){
        int writeData = 0;
        for(int i = 0; i < puntosAcceso.size(); i++){
            int val = puntosAcceso.get(i).intValue();
            Integer tempAccessPoint = new Integer(accessPoints.get(val).intValue() + 1);
            accessPoints.set(val, tempAccessPoint.intValue());
        }
        for(int i = 0; i < accessPoints.size(); i++){
            if(accessPoints.get(i).intValue() > 0){
                writeData |= (1 << i);
            }
        }
        for(int i = 0; i < devicesI2C.size(); i++){
            int writeData16 = (writeData >> (16*i));
            int lowerData = writeData16 & (0xFF);
            int upperData = (writeData16 >> 8) & (0xFF);
            try{
                devicesI2C.get(i).write(0x02, (byte)lowerData);
                devicesI2C.get(i).write(0x03, (byte)upperData);
            } catch(Exception ex){}
            i++;
        }
    }

    private synchronized void releaseRelays(List<Integer> puntosAcceso){
        int writeData = 0;
        for(int i = 0; i < puntosAcceso.size(); i++){
            int val = puntosAcceso.get(i).intValue();
            Integer tempAccessPoint = new Integer(accessPoints.get(val).intValue() - 1);
            accessPoints.set(val, tempAccessPoint.intValue());
        }
        for(int i = 0; i < accessPoints.size(); i++){
            if(accessPoints.get(i).intValue() > 0){
                writeData |= (1 << i);
            }
        }
        for(int i = 0; i < devicesI2C.size(); i++){
            int writeData16 = (writeData >> (16*i));
            int lowerData = writeData16 & (0xFF);
            int upperData = (writeData16 >> 8) & (0xFF);
            try{
                devicesI2C.get(i).write(0x02, (byte)lowerData);
                devicesI2C.get(i).write(0x03, (byte)upperData);
            } catch(Exception ex) {}
            i++;
        }
    }
}
