package cl.dreamit.elevateit.DataModel;

import java.util.List;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.UploadableEntity;

public class DataSincronizacionSubida {
    String tabla;
    List<UploadableEntity> data;

    public DataSincronizacionSubida(String tabla, List<UploadableEntity> data) {
        this.tabla = tabla;
        this.data = data;
    }
}
