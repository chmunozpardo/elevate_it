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

public enum Controladores {
    INSTANCE;

    public void save(List<Controlador> controladores){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Controlador> controladoresList = controladores;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<Controlador> it = controladoresList.iterator(); it.hasNext();) {
                Controlador enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public void save(Controlador controlador){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(controlador);
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public void save(Controlador[] controladores){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Controlador> controladoresList = Arrays.asList(controladores);
        try{
            entityManager.getTransaction().begin();
            for (Iterator<Controlador> it = controladoresList.iterator(); it.hasNext();) {
                Controlador enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public Controlador getByID(int id){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
