package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.Persona;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class Personas {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<Persona> personas) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Persona> personasList = personas;
        entityManager.getTransaction().begin();
        for (Iterator<Persona> it = personasList.iterator(); it.hasNext();) {
            Persona enquiry = it.next();
            entityManager.merge(enquiry);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static Persona getByCredencial(int idTipoCredencial, String credencial) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
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

    public static Persona getById(int id) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
