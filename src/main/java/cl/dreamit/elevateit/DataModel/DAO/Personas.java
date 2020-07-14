package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Persona;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public enum Personas {
    INSTANCE;

    public void save(List<Persona> personas) {
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Persona> personasList = personas;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<Persona> it = personasList.iterator(); it.hasNext();) {
                Persona enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public Persona getByCredencial(int idTipoCredencial, String credencial) {
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM Persona p " +
            "WHERE id_tipo_credencial = :idTipoCredencial " +
            "AND credencial LIKE :credencial"
        )
        .setParameter("idTipoCredencial", idTipoCredencial)
        .setParameter("credencial", credencial);
        Persona outputResult;
        try {
            outputResult = (Persona) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public Persona getById(int id) {
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM Persona p WHERE id = :id"
        )
        .setParameter("id", id);
        Persona outputResult;
        try {
            outputResult = (Persona) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
