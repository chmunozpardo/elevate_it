package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ConjuntoReserva;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public enum ConjuntosReservas {
    INSTANCE;

    public void save(List<ConjuntoReserva> conjuntosReservas){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ConjuntoReserva> reservasList = conjuntosReservas;
        try{
            entityManager.getTransaction().begin();
            for (Iterator<ConjuntoReserva> it = reservasList.iterator(); it.hasNext();) {
                ConjuntoReserva enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public ConjuntoReserva getByID(int id_conjunto_reserva){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM ConjuntoReserva c " +
            "WHERE id = :id_conjunto_reserva"
        )
        .setParameter("id_conjunto_reserva", id_conjunto_reserva);
        ConjuntoReserva outputResult;
        try{
            outputResult = (ConjuntoReserva) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public ConjuntoReserva getConjuntoReservaCodigoReserva(String codigoAcceso){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM ConjuntoReserva c " +
            "WHERE codigo_reserva LIKE :codigoAcceso"
        )
        .setParameter("codigoAcceso", codigoAcceso);
        ConjuntoReserva outputResult;
        try{
            outputResult = (ConjuntoReserva) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }
}
