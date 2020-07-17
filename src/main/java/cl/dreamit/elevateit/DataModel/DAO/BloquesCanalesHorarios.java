package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.BloqueHorario;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public enum BloquesCanalesHorarios {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized void save(BloqueHorario[] bloques){
        List<BloqueHorario> bloquesList = Arrays.asList(bloques);
        try{
            entityManager.getTransaction().begin();
            for (Iterator<BloqueHorario> it = bloquesList.iterator(); it.hasNext();) {
                BloqueHorario enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }

    public synchronized List<BloqueHorario> getByDiaHora(int id_canal_horario, String nombreDia, String horaActual) {
        Query query = entityManager.createQuery(
            "SELECT b FROM BloqueHorario b " +
            "WHERE id_canal_horario = :id_canal_horario " +
            "AND dia LIKE :nombreDia " +
            "AND inicio <= :horaActual " +
            "AND :horaActual <= fin " +
            "AND deleted_at IS NULL"
        )
        .setParameter("id_canal_horario", id_canal_horario)
        .setParameter("nombreDia", nombreDia)
        .setParameter("horaActual", horaActual);
        List<BloqueHorario> outputList;
        try{
            outputList = (List<BloqueHorario>) query.getResultList();
        } catch(NoResultException ex) {
            outputList = null;
        }
        return outputList;
    }
}
