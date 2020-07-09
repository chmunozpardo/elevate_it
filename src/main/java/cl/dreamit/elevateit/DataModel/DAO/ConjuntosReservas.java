package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.ConjuntoReserva;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Iterator;
import java.util.List;

public class ConjuntosReservas {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(List<ConjuntoReserva> conjuntosReservas){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ConjuntoReserva> reservasList = conjuntosReservas;
        entityManager.getTransaction().begin();
        for (Iterator<ConjuntoReserva> it = reservasList.iterator(); it.hasNext();) {
            ConjuntoReserva enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static ConjuntoReserva getByID(int id_conjunto_reserva){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
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

    public static ConjuntoReserva getConjuntoReservaCodigoReserva(String codigoAcceso){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
