package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.LogAcceso;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public enum LogsAcceso implements UploadableDAO<LogAcceso>{
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;

    public List<LogAcceso> getAll(){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM registros_acceso r"
        );
        List<LogAcceso> outputResult;
        try{
            outputResult = (List<LogAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public List<LogAcceso> getNewerThan(int lastID){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM LogAcceso r WHERE id > :lastID ORDER BY id ASC"
        )
        .setMaxResults(100)
        .setParameter("lastID", lastID);
        List<LogAcceso> outputResult;
        try{
            outputResult = (List<LogAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public List<LogAcceso> getNewerThan(long lastID, long offset){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT * FROM registros_acceso WHERE id > :lastID ORDER BY id ASC LIMIT :offset,100"
        )
        .setParameter("lastID", lastID)
        .setParameter("offset", offset);
        List<LogAcceso> outputResult;
        try{
            outputResult = (List<LogAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public void save(LogAcceso acceso){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(acceso);
        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public String getTable(){
        return "registros_acceso";
    }
}
