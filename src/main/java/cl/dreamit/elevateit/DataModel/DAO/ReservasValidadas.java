package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.ReservaValidada;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public enum ReservasValidadas implements UploadableDAO<ReservaValidada>{
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ReservaValidada r){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            try{
                entityManager.getTransaction().begin();
                entityManager.persist(r);
                entityManager.getTransaction().commit();
                entityManager.clear();
            } catch (Exception ex){
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    public List<ReservaValidada> getNewerThan(int lastID){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM ReservaValidada r WHERE id > :lastID ORDER BY id ASC"
            )
            .setMaxResults(100)
            .setParameter("lastID", lastID);
            List<ReservaValidada> outputResult;
            try {
                outputResult = (List<ReservaValidada>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public String getTable(){
        return "reserva_validada";
    }

}
