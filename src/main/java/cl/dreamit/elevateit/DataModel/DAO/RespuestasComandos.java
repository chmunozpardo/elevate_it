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

    public List<RespuestaComandoManual> getNewerThan(int lastID){
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

    public List<RespuestaComandoManual> getNewerThan(int lastID, long offset){
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

    public void save(RespuestaComandoManual r){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(r);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public String getTable(){
        return "respuestaComandoManual";
    }
}
