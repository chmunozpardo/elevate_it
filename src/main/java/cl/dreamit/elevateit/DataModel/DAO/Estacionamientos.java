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

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized void save(List<Estacionamiento> estacionamientos){
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
    }

    public synchronized Estacionamiento getByID(int id_estacionamiento){
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
        entityManager.clear();
        return outputResult;
    }
}
