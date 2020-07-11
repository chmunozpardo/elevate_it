package cl.dreamit.elevateit.Synchronizer;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import cl.dreamit.elevateit.DataModel.DataSincronizacionSubida;

public class SyncMessage {
    public Long timestamp;
    public List<DataSincronizacionSubida> uploadData = new ArrayList<DataSincronizacionSubida>();

    public SyncMessage(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
