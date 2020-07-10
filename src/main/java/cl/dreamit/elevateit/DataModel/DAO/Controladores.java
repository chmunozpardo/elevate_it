package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Controlador;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Controladores {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<Controlador> controladores){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Controlador> controladoresList = controladores;
        entityManager.getTransaction().begin();
        for (Iterator<Controlador> it = controladoresList.iterator(); it.hasNext();) {
            Controlador enquiry = it.next();
            entityManager.merge(enquiry);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void save(Controlador controlador){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(controlador);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void save(Controlador[] controladores){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Controlador> controladoresList = Arrays.asList(controladores);
        entityManager.getTransaction().begin();
        for (Iterator<Controlador> it = controladoresList.iterator(); it.hasNext();) {
            Controlador enquiry = it.next();
            entityManager.merge(enquiry);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static Controlador getByID(int id){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM Controlador c WHERE id = :id"
        )
        .setParameter("id", id);
        Controlador outputControlador;
        try{
            outputControlador = (Controlador) query.getSingleResult();
        } catch(NoResultException ex){
            outputControlador = null;
        }
        entityManager.close();
        return outputControlador;
    }
}
