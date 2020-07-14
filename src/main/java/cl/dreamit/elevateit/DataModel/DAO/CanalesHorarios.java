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

public enum CanalesHorarios {
    INSTANCE;

    public void save(CanalHorario[] canales){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<CanalHorario> canalesList = Arrays.asList(canales);
        try{
            entityManager.getTransaction().begin();
            for (Iterator<CanalHorario> it = canalesList.iterator(); it.hasNext();) {
                CanalHorario enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public CanalHorario getById(int id_canal_horario){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
