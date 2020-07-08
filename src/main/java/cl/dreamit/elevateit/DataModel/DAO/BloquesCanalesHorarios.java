package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.GK2.BloqueHorario;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class BloquesCanalesHorarios {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(BloqueHorario[] bloques){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<BloqueHorario> bloquesList = Arrays.asList(bloques);
        entityManager.getTransaction().begin();
        for (Iterator<BloqueHorario> it = bloquesList.iterator(); it.hasNext();) {
            BloqueHorario enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
    }

    public static List<BloqueHorario> getByDiaHora(int id_canal_horario, String nombreDia, String horaActual) {
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
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
        return (List<BloqueHorario>) query.getResultList();
    }
}