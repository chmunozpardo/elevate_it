package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ParametroControlador;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class ParametrosControladores {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<ParametroControlador> parametrosControlador){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ParametroControlador> parametrosControladorList = parametrosControlador;
        entityManager.getTransaction().begin();
        for (Iterator<ParametroControlador> it = parametrosControladorList.iterator(); it.hasNext();) {
            ParametroControlador enquiry = it.next();
            entityManager.merge(enquiry);
        }
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static void save(ParametroControlador parametro){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(parametro);
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static ParametroControlador getParametroControlador(int idControlador, String nombreParametro){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM ParametroControlador p WHERE id_controlador = :idControlador AND parametro LIKE :nombreParametro"
        )
        .setParameter("idControlador", idControlador)
        .setParameter("nombreParametro", nombreParametro);
        ParametroControlador outputResult;
        try{
            outputResult = (ParametroControlador) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
