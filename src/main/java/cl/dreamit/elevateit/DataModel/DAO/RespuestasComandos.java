package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.RespuestaComandoManual;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public class RespuestasComandos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static List<RespuestaComandoManual> getAll(){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM RespuestaComandoManual r"
        );
        return (List<RespuestaComandoManual>) query.getResultList();
    }

    public static List<RespuestaComandoManual> getNewerThan(int lastID){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM RespuestaComandoManual r WHERE id > :lastID ORDER BY id ASC"
        )
        .setMaxResults(100)
        .setParameter("lastID", lastID);
        return (List<RespuestaComandoManual>) query.getResultList();
    }

    public static List<RespuestaComandoManual> getNewerThan(int lastID, long offset){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM RespuestaComandoManual r WHERE id > :lastID ORDER BY id ASC LIMIT :offset,100"
        )
        .setParameter("lastID", lastID)
        .setParameter("offset", offset);
        return (List<RespuestaComandoManual>) query.getResultList();
    }

    public static void save(RespuestaComandoManual r){

    }

    public static String getTable(){
        return "respuestaComandoManual";
    }
}
