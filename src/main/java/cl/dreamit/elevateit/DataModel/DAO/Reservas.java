package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class Reservas {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(Reserva[] reservas){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<Reserva> reservasList = Arrays.asList(reservas);
        entityManager.getTransaction().begin();
        for (Iterator<Reserva> it = reservasList.iterator(); it.hasNext();) {
            Reserva enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
    }

    public static List<Reserva> getAll(){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM Reserva r"
        );
        return (List<Reserva>) query.getResultList();
    }

    public static List<Reserva> getReservasValidasFecha(String fechaValidez) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM Reserva r " +
            "WHERE fecha_inicio <= :fechaValidez " +
            "AND fecha_fin >= :fechaValidez"
        )
        .setParameter("fechaValidez", fechaValidez)
        .setParameter("fechaValidez", fechaValidez);
        return (List<Reserva>) query.getResultList();
    }

    public static List<Reserva> getReservasPersonaFecha(int id_persona, String fechaValidez){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM Reserva r " +
            "WHERE fecha_inicio_index <= :fechaValidez " +
            "AND fecha_fin_index >= :fechaValidez " +
            "AND id_persona_reservada = :id_persona"
        )
        .setParameter("fechaValidez", fechaValidez)
        .setParameter("id_persona", id_persona);
        return (List<Reserva>) query.getResultList();
    }

    public static List<Reserva> getReservasByIdConjuntoReserva(int idConjuntoReserva) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM Reserva r " +
            "WHERE id_conjunto_reserva = :idConjuntoReserva"
        )
        .setParameter("idConjuntoReserva", idConjuntoReserva);
        return (List<Reserva>) query.getResultList();
    }

    public static List<Reserva> getReservasByIdConjuntoReserva(int idConjuntoReserva, String fechaValidez){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT * FROM reserva " +
            "WHERE id_conjunto_reserva = :idConjuntoReserva " +
            "AND fecha_inicio_index <= :fechaValidez" +
            "AND fecha_fin_index >= :fechaValidez"
        )
        .setParameter("idConjuntoReserva", idConjuntoReserva)
        .setParameter("fechaValidez", fechaValidez);
        return (List<Reserva>) query.getResultList();
    }
}
