package cl.dreamit.elevateit.Hardware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import cl.dreamit.elevateit.Configuration.CONF;
import cl.dreamit.elevateit.DataModel.DAO.PuntosAccesos;

public enum Relay {
    INSTANCE;

    private List<I2CDevice> devicesI2C = new ArrayList<I2CDevice>();;
    private List<Integer> accessPoints = new ArrayList<Integer>();

    // I2C registers
    // 0, "Input port 0"
    // 1, "Input port 1"
    // 2, "Output port 0"
    // 3, "Output port 1"
    // 4, "Polarity inversion port 0"
    // 5, "Polarity inversion port 1"
    // 6, "Configuration port 0"
    // 7, "Configuration port 1"

    private void sleepRelay(){
        int sleepTime = PuntosAccesos.INSTANCE.getPuntoAccesoControlador(0).tiempoApertura();
        try{
            Thread.sleep(sleepTime*1000);
        } catch(Exception ex){}
    }

    public void setup() {
        try{
            I2CBus busI2C = I2CFactory.getInstance(I2CBus.BUS_0);
            CONF.CANTIDAD_CANALES = 0;
            for(int address = 0x20; address < 0x28; address++){
                try{
                    I2CDevice i2cdevice = busI2C.getDevice(address);
                    i2cdevice.write(0x02, (byte)0x00);
                    i2cdevice.write(0x03, (byte)0x00);
                    i2cdevice.write(0x06, (byte)0x00);
                    i2cdevice.write(0x07, (byte)0x00);
                    devicesI2C.add(i2cdevice);
                    for(int i = 0; i < 16; i++){
                        accessPoints.add(0);
                        CONF.CANTIDAD_CANALES += 1;
                    }
                } catch(IOException ex) {
                    continue;
                }
            }
        } catch(Exception ex){}
    }

    public void openRelay(int puntoAcceso) {
        new Thread(() -> {
            try{
                incAccessPoint(puntoAcceso);
                setRelays();
                sleepRelay();
                decAccessPoint(puntoAcceso);
                setRelays();
            } catch(Exception ignored) {}
        }).start();
    }

    public void openRelay(List<Integer> puntosAcceso) {
        new Thread(() -> {
            try{
                incAccessPoints(puntosAcceso);
                setRelays();
                sleepRelay();
                decAccessPoints(puntosAcceso);
                setRelays();
            } catch(Exception ignored) {}
        }).start();
    }

    private void incAccessPoint(int puntoAcceso){
        synchronized(accessPoints){
            Integer tempAccessPoint = new Integer(accessPoints.get(puntoAcceso).intValue() + 1);
            accessPoints.set(puntoAcceso, tempAccessPoint.intValue());
        }
    }

    private void incAccessPoints(List<Integer> puntosAcceso){
        synchronized(accessPoints){
            for(int i = 0; i < puntosAcceso.size(); i++){
                int val = puntosAcceso.get(i).intValue();
                Integer tempAccessPoint = new Integer(accessPoints.get(val).intValue() + 1);
                accessPoints.set(val, tempAccessPoint.intValue());
            }
        }
    }

    private void decAccessPoint(int puntoAcceso){
        synchronized(accessPoints){
            Integer tempAccessPoint = new Integer(accessPoints.get(puntoAcceso).intValue() - 1);
            accessPoints.set(puntoAcceso, tempAccessPoint.intValue());
        }
    }

    private void decAccessPoints(List<Integer> puntosAcceso){
        synchronized(accessPoints){
            for(int i = 0; i < puntosAcceso.size(); i++){
                int val = puntosAcceso.get(i).intValue();
                Integer tempAccessPoint = new Integer(accessPoints.get(val).intValue() - 1);
                accessPoints.set(val, tempAccessPoint.intValue());
            }
        }
    }

    private synchronized void setRelays(){
        int writeData = 0;
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

    public synchronized void openAll(){
        for(int i = 0; i < devicesI2C.size(); i++){
            try{
                devicesI2C.get(i).write(0x02, (byte)0xFF);
                devicesI2C.get(i).write(0x03, (byte)0xFF);
            } catch(Exception ex){}
            i++;
        }
    }

    public synchronized void closeAll(){
        for(int i = 0; i < devicesI2C.size(); i++){
            try{
                devicesI2C.get(i).write(0x02, (byte)0x00);
                devicesI2C.get(i).write(0x03, (byte)0x00);
            } catch(Exception ex){}
            i++;
        }
    }
}
