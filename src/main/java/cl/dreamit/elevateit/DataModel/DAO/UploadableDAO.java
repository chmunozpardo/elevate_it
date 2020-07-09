package cl.dreamit.elevateit.DataModel.DAO;

import java.util.List;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.UploadableEntity;

public interface UploadableDAO<T extends UploadableEntity> {
    public String getTable();
    public List<T> getNewerThan(int lastID);
}
