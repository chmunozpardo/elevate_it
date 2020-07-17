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
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized List<LogAcceso> getAll(){
        Query query = entityManager.createQuery(
            "SELECT r FROM LogAcceso r"
        );
        List<LogAcceso> outputResult;
        try{
            outputResult = (List<LogAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        return outputResult;
    }

    public synchronized List<LogAcceso> getNewerThan(int lastID){
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
        return outputResult;
    }

    public synchronized List<LogAcceso> getNewerThan(long lastID, long offset){
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
        return outputResult;
    }

    public synchronized void save(LogAcceso acceso){
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(acceso);
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }

    public synchronized void clean(){
        Query query = entityManager.createQuery(
            "DELETE l FROM LogAcceso l WHERE l.fecha_registro < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 180 DAY))"
        );
        try{
            query.executeUpdate();
        } catch (Exception ex){
            System.out.println("Couldn't clean");
        }
    }

    public String getTable(){
        return "registros_acceso";
    }
}
