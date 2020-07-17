package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.Utils.Util;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public enum LogsInternos {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();;

    public synchronized void save(LogInterno log){
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(log);
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }

    public synchronized void clean(){
        try{
            LocalDateTime date = LocalDateTime.now().minusDays(14);
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(
                "DELETE FROM LogInterno l WHERE l.fecha < :date"
            )
            .setParameter("date", Util.getDateTime(date));
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            System.out.println("Couldn't clean" + ex);
        }
    }
}
