package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.ReservaValidada;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public class ReservasValidadas {

    @PersistenceContext
    private static EntityManager entityManager;

    public void save(ReservaValidada r){

    }

    public static List<ReservaValidada> getNewerThan(int lastID){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM ReservaValidada r WHERE id > :lastID ORDER BY id ASC"
        )
        .setMaxResults(100)
        .setParameter("lastID", lastID);
        return (List<ReservaValidada>) query.getResultList();
    }

    public static String getTable(){
        return "reserva_validada";
    }

}
