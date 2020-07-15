package cl.dreamit.elevateit.Hardware;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Buzzer {

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

    public static void main(String[] args) {
        buzz(notesTable.get("C5"), 500);
        buzz(notesTable.get("E5"), 500);
        buzz(notesTable.get("G5"), 500);
        buzz(notesTable.get("E5"), 500);
        buzz(notesTable.get("C5"), 500);
    }
}