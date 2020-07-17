package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import cl.dreamit.elevateit.Utils.PersistenceManager;

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
        Query query = entityManager.createQuery(
            "DELETE l FROM LogInterno l WHERE l.fecha < UNIX_TIMESTAMP(DATE_SUB(NOW(), INTERVAL 180 DAY))"
        );
        try{
            query.executeUpdate();
        } catch (Exception ex){
            System.out.println("Couldn't clean");
        }
    }
}
