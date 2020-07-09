package cl.dreamit.elevateit.DataModel.DAO;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ComandoManual;

public class ComandosManuales {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(ComandoManual[] comandos){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        List<ComandoManual> comandosList = Arrays.asList(comandos);
        for (Iterator<ComandoManual> it = comandosList.iterator(); it.hasNext();) {
            ComandoManual enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
