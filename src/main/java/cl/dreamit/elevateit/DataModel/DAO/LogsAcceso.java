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
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM LogAcceso r"
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
    }

    public List<LogAcceso> getNewerThan(int lastID){
        synchronized(this){
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
    }

    public List<LogAcceso> getNewerThan(long lastID, long offset){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM LogAcceso r WHERE id > :lastID ORDER BY id ASC"
            )
            .setFirstResult((int) offset)
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
    }

    public void save(LogAcceso acceso){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            try{
                entityManager.getTransaction().begin();
                entityManager.persist(acceso);
                entityManager.getTransaction().commit();
                entityManager.clear();
            } catch (Exception ex){
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    public String getTable(){
        return "registros_acceso";
    }
}
