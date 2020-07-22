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

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized void save(List<ConjuntoReserva> conjuntosReservas){
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
    }

    public synchronized ConjuntoReserva getByID(int id_conjunto_reserva){
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
        entityManager.clear();
        return outputResult;
    }

    public synchronized ConjuntoReserva getConjuntoReservaCodigoReserva(String codigoAcceso){
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
        entityManager.clear();
        return outputResult;
    }
}
