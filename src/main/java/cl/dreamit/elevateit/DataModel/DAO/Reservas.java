package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.Reserva;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public enum Reservas {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Reserva[] reservas){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            List<Reserva> reservasList = Arrays.asList(reservas);
            try{
                entityManager.getTransaction().begin();
                for (Iterator<Reserva> it = reservasList.iterator(); it.hasNext();) {
                    Reserva enquiry = it.next();
                    entityManager.merge(enquiry);
                }
                entityManager.getTransaction().commit();
                entityManager.clear();
            } catch (Exception ex){
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }

    public List<Reserva> getAll(){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM Reserva r"
            );
            List<Reserva> outputResult;
            try {
                outputResult = (List<Reserva>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<Reserva> getReservasValidasFecha(String fechaValidez) {
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM Reserva r " +
                "WHERE fecha_inicio <= :fechaValidez " +
                "AND fecha_fin >= :fechaValidez"
            )
            .setParameter("fechaValidez", fechaValidez)
            .setParameter("fechaValidez", fechaValidez);
            List<Reserva> outputResult;
            try {
                outputResult = (List<Reserva>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<Reserva> getReservasPersonaFecha(int id_persona, String fechaValidez){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM Reserva r " +
                "WHERE fecha_inicio_index <= :fechaValidez " +
                "AND fecha_fin_index >= :fechaValidez " +
                "AND id_persona_reservada = :id_persona"
            )
            .setParameter("fechaValidez", fechaValidez)
            .setParameter("id_persona", id_persona);
            List<Reserva> outputResult;
            try {
                outputResult = (List<Reserva>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<Reserva> getReservasByIdConjuntoReserva(int idConjuntoReserva) {
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM Reserva r " +
                "WHERE id_conjunto_reserva = :idConjuntoReserva"
            )
            .setParameter("idConjuntoReserva", idConjuntoReserva);
            List<Reserva> outputResult;
            try {
                outputResult = (List<Reserva>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }

    public List<Reserva> getReservasByIdConjuntoReserva(int idConjuntoReserva, String fechaValidez){
        synchronized(this){
            entityManager = PersistenceManager.INSTANCE.getEntityManager();
            Query query = entityManager.createQuery(
                "SELECT r FROM Reserva r " +
                "WHERE id_conjunto_reserva = :idConjuntoReserva " +
                "AND fecha_inicio_index <= :fechaValidez" +
                "AND fecha_fin_index >= :fechaValidez"
            )
            .setParameter("idConjuntoReserva", idConjuntoReserva)
            .setParameter("fechaValidez", fechaValidez);
            List<Reserva> outputResult;
            try {
                outputResult = (List<Reserva>) query.getResultList();
            } catch(NoResultException ex) {
                outputResult = null;
            }
            entityManager.close();
            return outputResult;
        }
    }
}
