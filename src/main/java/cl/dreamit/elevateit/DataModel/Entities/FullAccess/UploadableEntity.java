package cl.dreamit.elevateit.DataModel.Entities.FullAccess;

/**
 * Clase que estandariza aquellas Entity que pueden ser cargadas por un UploadableDAO
 */
public abstract class UploadableEntity {
    public abstract String getTable();
    public abstract long getID();
}
