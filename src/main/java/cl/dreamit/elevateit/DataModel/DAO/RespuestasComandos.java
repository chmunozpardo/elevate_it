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
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized List<RespuestaComandoManual> getAll(){
        Query query = entityManager.createQuery(
            "SELECT r FROM RespuestaComandoManual r"
        );
        List<RespuestaComandoManual> outputResult;
        try {
            outputResult = (List<RespuestaComandoManual>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        return outputResult;
    }

    public synchronized List<RespuestaComandoManual> getNewerThan(int lastID){
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
        return outputResult;
    }

    public synchronized List<RespuestaComandoManual> getNewerThan(int lastID, long offset){
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
        return outputResult;
    }

    public synchronized void save(RespuestaComandoManual r){
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(r);
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }

    public String getTable(){
        return "respuestaComandoManual";
    }
}
