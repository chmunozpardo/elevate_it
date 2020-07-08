package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.LogAcceso;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public class LogsAcceso {

    @PersistenceContext
    private static EntityManager entityManager;

    public static List<LogAcceso> getAll(){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM registros_acceso r"
        );
        return (List<LogAcceso>) query.getResultList();
    }

    public static List<LogAcceso> getNewerThan(int lastID){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM LogAcceso r WHERE id > :lastID ORDER BY id ASC"
        )
        .setMaxResults(100)
        .setParameter("lastID", lastID);
        return (List<LogAcceso>) query.getResultList();
    }

    public static List<LogAcceso> getNewerThan(long lastID, long offset){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT * FROM registros_acceso WHERE id > :lastID ORDER BY id ASC LIMIT :offset,100"
        )
        .setParameter("lastID", lastID)
        .setParameter("offset", offset);
        return (List<LogAcceso>) query.getResultList();
    }

    public static void save(LogAcceso acceso){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(acceso);
        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
    }

    public static String getTable(){
        return "registros_acceso";
    }
}
