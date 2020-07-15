package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.FullAccess.LogInterno;

import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public enum LogsInternos {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;

    public void save(LogInterno log){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            try{
                entityManager.getTransaction().begin();
                entityManager.persist(log);
                entityManager.getTransaction().commit();
                entityManager.clear();
            } catch (Exception ex){
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
}
