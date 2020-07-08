package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class PuntosAccesos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<PuntoAcceso> puntos){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<PuntoAcceso> reservasList = puntos;
        entityManager.getTransaction().begin();
        for (Iterator<PuntoAcceso> it = reservasList.iterator(); it.hasNext();) {
            PuntoAcceso enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
    }

    public static PuntoAcceso getPuntoAccesoControlador(int id, int canal){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p WHERE id_controlador = :id AND numeroCanal = :canal"
        )
        .setParameter("id", id)
        .setParameter("canal", canal);
        return (PuntoAcceso) query.getSingleResult();
    }

    public static List<PuntoAcceso> getPuntosAccesoControlador(int id){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p WHERE id_controlador = :id"
        )
        .setParameter("id", id);
        return (List<PuntoAcceso>) query.getResultList();
    }
}
