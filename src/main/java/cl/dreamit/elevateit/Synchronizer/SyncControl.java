package cl.dreamit.elevateit.Synchronizer;

public enum SyncControl {
    INSTANCE;

    private boolean runSync = true;

    private SyncControl(){
    }

    public synchronized boolean getState(){
        return runSync;
    }

    public synchronized void offSync(){
        runSync = false;
    }

    public synchronized void onSync(){
        runSync = true;
    }
}