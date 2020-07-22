package cl.dreamit.elevateit.DataModel.DAO;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ComandoManual;

public enum ComandosManuales {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized void save(ComandoManual[] comandos){
        List<ComandoManual> comandosList = Arrays.asList(comandos);
        try{
            entityManager.getTransaction().begin();
            for (Iterator<ComandoManual> it = comandosList.iterator(); it.hasNext();) {
                ComandoManual enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }
}
