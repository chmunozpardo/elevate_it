package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.RespuestaComandoManual;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public enum RespuestasComandos implements UploadableDAO<RespuestaComandoManual>{
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;

    public List<RespuestaComandoManual> getAll(){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM RespuestaComandoManual r"
            );
            List<RespuestaComandoManual> outputResult;
            try {
                outputResult = (List<RespuestaComandoManual>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<RespuestaComandoManual> getNewerThan(int lastID){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM RespuestaComandoManual r WHERE id > :lastID ORDER BY id ASC"
            )
            .setMaxResults(100)
            .setParameter("lastID", lastID);
            List<RespuestaComandoManual> outputResult;
            try {
                outputResult = (List<RespuestaComandoManual>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<RespuestaComandoManual> getNewerThan(int lastID, long offset){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM RespuestaComandoManual r WHERE id > :lastID ORDER BY id ASC"
            )
            .setFirstResult((int) offset)
            .setMaxResults(100)
            .setParameter("lastID", lastID);
            List<RespuestaComandoManual> outputResult;
            try {
                outputResult = (List<RespuestaComandoManual>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public void save(RespuestaComandoManual r){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            try{
                entityManager.getTransaction().begin();
                entityManager.merge(r);
                entityManager.getTransaction().commit();
                entityManager.clear();
            } catch (Exception ex){
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    public String getTable(){
        return "respuestaComandoManual";
    }
}
