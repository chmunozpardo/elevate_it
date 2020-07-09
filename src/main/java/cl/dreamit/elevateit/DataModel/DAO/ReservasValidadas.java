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
    private static EntityManager entityManager;

    public void save(ReservaValidada r){
    }

    public List<ReservaValidada> getNewerThan(int lastID){
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

    public String getTable(){
        return "reserva_validada";
    }

}
