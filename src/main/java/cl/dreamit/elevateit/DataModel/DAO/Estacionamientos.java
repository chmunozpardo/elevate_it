package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Estacionamiento;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class Estacionamientos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<Estacionamiento> estacionamientos){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Estacionamiento> reservasList = estacionamientos;
        entityManager.getTransaction().begin();
        for (Iterator<Estacionamiento> it = reservasList.iterator(); it.hasNext();) {
            Estacionamiento enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static Estacionamiento getByID(int id_estacionamiento){
        Query query = entityManager.createQuery(
            "SELECT e FROM Estacionamiento e WHERE id = :id_estacionamiento"
        )
        .setParameter("id_estacionamiento", id_estacionamiento);
        Estacionamiento outputResult;
        try{
            outputResult = (Estacionamiento) query.getSingleResult();
        } catch(NoResultException ex){
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
