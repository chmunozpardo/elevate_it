package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.PuntoAcceso;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public enum PuntosAccesos {
    INSTANCE;

    @PersistenceContext(unitName="elevateIT")
    private EntityManager entityManager;

    public void save(List<PuntoAcceso> puntos){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<PuntoAcceso> reservasList = puntos;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<PuntoAcceso> it = reservasList.iterator(); it.hasNext();) {
                PuntoAcceso enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public PuntoAcceso getPuntoAccesoControlador(int id, int canal){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p WHERE id_controlador = :id AND numeroCanal = :canal"
        )
        .setParameter("id", id)
        .setParameter("canal", canal);
        PuntoAcceso outputResult;
        try {
            outputResult = (PuntoAcceso) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public List<PuntoAcceso> getPuntosAccesoControlador(int id){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p WHERE id_controlador = :id"
        )
        .setParameter("id", id);
        List<PuntoAcceso> outputResult;
        try {
            outputResult = (List<PuntoAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
