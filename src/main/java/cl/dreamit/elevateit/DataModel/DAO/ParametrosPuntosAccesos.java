package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.ParametroPuntoAcceso;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class ParametrosPuntosAccesos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<ParametroPuntoAcceso> parametrosPuntosAccesos){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ParametroPuntoAcceso> parametrosPuntosAccesosList = parametrosPuntosAccesos;
        entityManager.getTransaction().begin();
        for (Iterator<ParametroPuntoAcceso> it = parametrosPuntosAccesosList.iterator(); it.hasNext();) {
            ParametroPuntoAcceso enquiry = it.next();
            entityManager.merge(enquiry);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static ParametroPuntoAcceso getParametroPuntoAcceso(int idPuntoAcceso, String nombreParametro){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM ParametroPuntoAcceso p WHERE id_punto_acceso = :idPuntoAcceso AND parametro LIKE :nombreParametro"
        )
        .setParameter("idPuntoAcceso", idPuntoAcceso)
        .setParameter("nombreParametro", nombreParametro);
        ParametroPuntoAcceso outputResult;
        try {
            outputResult = (ParametroPuntoAcceso) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
