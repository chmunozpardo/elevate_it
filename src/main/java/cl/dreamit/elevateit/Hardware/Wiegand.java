package cl.dreamit.elevateit.Hardware;

import cl.dreamit.elevateit.AccessControl.ProcesadorComandosManuales;
import cl.dreamit.elevateit.Synchronizer.SyncControl;

public class Wiegand implements Runnable{
    static {
        System.loadLibrary("elevateit");
    }

    private int card_1 = 0;
    private int card_2 = 0;
    private int cardType = 0;

    public native void readCard();

    public Wiegand(){
    }

    public void print(){
        System.out.println(
            "Card:\n" +
            "  - code_1 = " + this.card_1 + "\n" +
            "  - code_2 = " + this.card_2 + "\n" +
            "  - cardType = " + this.cardType
        );
    }

    public void run() {
        while(true){
            if(SyncControl.INSTANCE.getState()){
                readCard();
                if(SyncControl.INSTANCE.getState()){
                    ProcesadorComandosManuales.INSTANCE.searchCard(
                        card_1,
                        card_2,
                        cardType
                    );
                }
            }
        }
    }
}
