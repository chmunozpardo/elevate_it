package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Estacionamiento;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public enum Estacionamientos {
    INSTANCE;

    public void save(List<Estacionamiento> estacionamientos){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Estacionamiento> reservasList = estacionamientos;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<Estacionamiento> it = reservasList.iterator(); it.hasNext();) {
                Estacionamiento enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public Estacionamiento getByID(int id_estacionamiento){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
