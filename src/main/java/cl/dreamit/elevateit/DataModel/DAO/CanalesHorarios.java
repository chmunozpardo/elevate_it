package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.CanalHorario;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CanalesHorarios {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(CanalHorario[] canales){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<CanalHorario> canalesList = Arrays.asList(canales);
        entityManager.getTransaction().begin();
        for (Iterator<CanalHorario> it = canalesList.iterator(); it.hasNext();) {
            CanalHorario enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static CanalHorario getById(int id_canal_horario){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM CanalHorario c " +
            "WHERE id = :id_canal_horario " +
            "AND deleted_at IS NULL"
        )
        .setParameter("id_canal_horario", id_canal_horario);
        CanalHorario outputCanalHorario;
        try {
            outputCanalHorario = (CanalHorario) query.getSingleResult();
        } catch (NoResultException ex){
            outputCanalHorario = null;
        }
        entityManager.close();
        return outputCanalHorario;
    }
}
