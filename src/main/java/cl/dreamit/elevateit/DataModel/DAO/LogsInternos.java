package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class LogsInternos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(LogInterno log){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(log);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }
}
