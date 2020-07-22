package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.Utils.PersistenceManager;
import cl.dreamit.elevateit.DataModel.Entities.FullAccess.Configuracion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@SuppressWarnings("unchecked")
public enum Configuraciones {
    INSTANCE;

    @PersistenceContext
    private EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();

    public synchronized List<Configuracion> getAll(){
        Query query = entityManager.createQuery(
            "SELECT c FROM Configuracion c"
        );
        List<Configuracion> outConfiguracion = null;
        try {
            outConfiguracion = (List<Configuracion>) query.getResultList();
        } catch(NoResultException ex) {
            outConfiguracion = null;
        }
        entityManager.clear();
        return outConfiguracion;
    }

    public synchronized Configuracion getParametro(String parametro){
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
        entityManager.clear();
        return outConfiguracion;
    }

    public synchronized void save(Configuracion p){
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(p);
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
    }
}
