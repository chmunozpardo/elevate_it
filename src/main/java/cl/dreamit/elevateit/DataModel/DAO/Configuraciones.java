package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.Configuracion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public class Configuraciones {

    @PersistenceContext
    private static EntityManager entityManager;

    public static List<Configuracion> getAll(){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM Configuracion c"
        );
        List<Configuracion> outConfiguracion = null;
        try {
            outConfiguracion = (List<Configuracion>) query.getResultList();
        } catch(NoResultException ex) {
            outConfiguracion = null;
        }
        entityManager.close();
        return outConfiguracion;
    }

    public static Configuracion getParametro(String parametro){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT c FROM Configuracion c WHERE parametro LIKE :parametro"
        )
        .setParameter("parametro", parametro);
        Configuracion outConfiguracion = null;
        try {
            outConfiguracion = (Configuracion) query.getSingleResult();
        } catch(NoResultException ex) {
            outConfiguracion = null;
        }
        entityManager.close();
        return outConfiguracion;
    }

    public static void save(Configuracion p){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(p);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
