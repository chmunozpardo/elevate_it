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

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized void save(List<PuntoAcceso> puntos){
        List<PuntoAcceso> reservasList = puntos;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<PuntoAcceso> it = reservasList.iterator(); it.hasNext();) {
                PuntoAcceso enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }

    public synchronized PuntoAcceso getPuntoAccesoControlador(int canal){
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p WHERE numeroCanal = :canal"
        )
        .setParameter("canal", canal);
        PuntoAcceso outputResult;
        try {
            outputResult = (PuntoAcceso) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        return outputResult;
    }

    public synchronized List<PuntoAcceso> getPuntosAccesoControlador(){
        Query query = entityManager.createQuery(
            "SELECT p FROM PuntoAcceso p"
        );
        List<PuntoAcceso> outputResult;
        try {
            outputResult = (List<PuntoAcceso>) query.getResultList();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        return outputResult;
    }
}
